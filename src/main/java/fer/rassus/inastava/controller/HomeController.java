package fer.rassus.inastava.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import fer.rassus.inastava.entity.AnswerEntity;
import fer.rassus.inastava.entity.QuestionEntity;
import fer.rassus.inastava.entity.SurveyEntity;
import fer.rassus.inastava.entity.UserEntity;
import fer.rassus.inastava.jpa.AnswerRepository;
import fer.rassus.inastava.jpa.QuestionRepository;
import fer.rassus.inastava.jpa.SurveyRepository;
import fer.rassus.inastava.jpa.UserRepository;
import fer.rassus.inastava.model.Answer;
import fer.rassus.inastava.model.Survey;
import fer.rassus.inastava.rest.AnswerController;
import fer.rassus.inastava.rest.QuestionController;
import fer.rassus.inastava.rest.SurveyController;
import fer.rassus.inastava.rest.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller

public class HomeController {
    private Environment environment;

    private UserRepository userRepository;
    private SurveyRepository surveyRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    private UserController userController;
    private SurveyController surveyController;
    private QuestionController questionController;
    private AnswerController answerController;

    @Autowired
    public HomeController(Environment environment, UserRepository userRepository, SurveyRepository surveyRepository,
                          QuestionRepository questionRepository, AnswerRepository answerRepository,
                          UserController userController, SurveyController surveyController,
                          QuestionController questionController, AnswerController answerController) {
        this.environment = environment;
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userController = userController;
        this.surveyController = surveyController;
        this.questionController = questionController;
        this.answerController = answerController;
    }

    @GetMapping("/")
    public String homepage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getName().equals("anonymousUser")) {
            return getUserOverview(model);
        } else {
            List<SurveyEntity> surveys = surveyRepository.findAll()
                    .stream()
                    .filter(SurveyEntity::getIsActive)
                    .collect(Collectors.toList());
            model.addAttribute("surveys", surveys);
            return "home";
        }
    }

    @GetMapping("/login")
    public String login(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getName().equals("anonymousUser")) {
            String loggedInUser = auth.getName();
            model.addAttribute("korisnik", loggedInUser);

            return "already-logged-in";
        } else {
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "home";
    }

    @GetMapping("/user-overview")
    public String getUserOverview(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = auth.getName();

        List<SurveyEntity> surveys = surveyRepository
                .getSurveysByUser(
                        userRepository
                                .findByUsername(loggedInUser)
                                .getId()
                );

        model.addAttribute("surveys", surveys);
        return "user-overview";
    }

    @GetMapping("/survey/add")
    public String addSurvey(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = auth.getName();

        UserEntity userEntity = userRepository.findByUsername(loggedInUser);
        model.addAttribute("userId", userEntity.getId());
        return "add-survey.html";
    }

    @RequestMapping(value = "/createSurvey", method = RequestMethod.POST)
    public String createSurvey(Model model, @ModelAttribute(value = "survey") Survey survey) {
        addNewSurvey(survey);

        return getUserOverview(model);
    }

    private void addNewSurvey(Survey survey) {
        String name = survey.getName();
        String description = survey.getDescription();
        String isActiveString = survey.getIsActiveString();
        boolean isActive = isActiveString != null && (isActiveString.equals("on"));
        List<String> questions = survey.getQuestions();
        List<String> answers = survey.getAnswers();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = auth.getName();
        UserEntity userEntity = userRepository.findByUsername(loggedInUser);

        Map<String, List<String>> questionAnswers = new HashMap<>();
        for (int i = 0, j = 0; i < questions.size(); i++) {
            List<String> questionsList = new ArrayList<>();
            for (int k = j; k < (j + 4); k++) {
                questionsList.add(answers.get(k));
            }
            questionAnswers.put(questions.get(i), questionsList);
            j += 4;
        }

        SurveyEntity surveyEntity = new SurveyEntity();
        surveyEntity.setSurveyName(name);
        surveyEntity.setDescription(description);
        surveyEntity.setUser(userEntity);
        surveyEntity.setIsActive(isActive);
        surveyController.addSurvey(surveyEntity);


        List<QuestionEntity> questionEntities = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : questionAnswers.entrySet()) {
            QuestionEntity questionEntity = new QuestionEntity();

            questionEntity.setQuestionText(entry.getKey());
            questionEntity.setSurvey(surveyEntity);
            questionController.addSurveyQuestion(surveyEntity.getId(), questionEntity);

            List<String> possibleAnswers = entry.getValue();
            List<AnswerEntity> answerEntities = new ArrayList<>();
            for (String possibleAnswer : possibleAnswers) {
                AnswerEntity answerEntity = new AnswerEntity();
                answerEntity.setAnswerText(possibleAnswer);
                answerEntity.setQuestion(questionEntity);
                answerEntity.setVotes(0);
                answerController.addSurveyQuestion(questionEntity.getId(), answerEntity);

                answerEntities.add(answerEntity);
            }
            questionEntity.setAnswers(answerEntities);

            questionEntities.add(questionEntity);
        }

        surveyEntity.setQuestions(questionEntities);

        List<SurveyEntity> userSurveys = userEntity.getSurveys();
        userSurveys.add(surveyEntity);
        userEntity.setSurveys(userSurveys);
    }


    @GetMapping("/survey-overview/{id}")
    public String surveyOverview(@PathVariable("id") long id, Model model) {

        SurveyEntity surveyEntity = surveyRepository.findById(id).orElse(null);
        if (surveyEntity == null) {
            model.addAttribute("message", "Anketa sa danim ID-em ne postoji");
        } else {
            model.addAttribute("surveyId", id);
            model.addAttribute("surveyName", surveyEntity.getSurveyName());
            model.addAttribute("surveyDescription", surveyEntity.getDescription());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUser = auth.getName();

            if (loggedInUser!=null && loggedInUser.equals(surveyEntity.getUser().getUsername())) {
                if (surveyEntity.getIsActive()) {
                    model.addAttribute("activity", "Promijenite anketu u neaktivnu.");
                    model.addAttribute("flag", "false");
                }
                else {
                    model.addAttribute("activity", "Promijenite anketu u aktivnu.");
                    model.addAttribute("flag", "true");
                }
            }
        }

        return "survey-overview";
    }

    @RequestMapping(value = "/generateQR/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQr(@PathVariable("id") long id) throws UnknownHostException {

        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        //hostAddress = environment.getProperty("serverUrl");
        int port = Integer.parseInt(environment.getProperty("server.port"));

        String text = "http://" + hostAddress + ":" + port + "/survey/" + id;
        //String text = "http://" + hostAddress  + "/survey/" + id;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);

            byte[] imageContent = byteArrayOutputStream.toByteArray();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/survey/{id}")
    public String viewSurvey(@PathVariable("id") long id, Model model) {

        SurveyEntity surveyEntity = surveyRepository.findById(id).orElse(null);
        if (surveyEntity == null) {
            model.addAttribute("message", "Anketa sa danim ID-em ne postoji");
        }
        else if (!surveyEntity.getIsActive()) {
            model.addAttribute("message", "Odabrana anketa je trenutno nedostupna");
        }
        else {
            model.addAttribute("surveyId", id);
            model.addAttribute("surveyName", surveyEntity.getSurveyName());
            model.addAttribute("surveyDescription", surveyEntity.getDescription());

            Map<String, List<AnswerEntity>> questionAndAnswers = new HashMap<>();

            List<QuestionEntity> questions = questionRepository.getQuestionsBySurvey(id);
            questions.forEach(question -> questionAndAnswers.put(question.getQuestionText(), question.getAnswers()));

            model.addAttribute("questionsAndAnswers", questionAndAnswers);
        }

        return "view-survey";
    }

    @RequestMapping(value = "/surveyAnswers/{id}", method = RequestMethod.POST)
    public String surveyAnswers(@PathVariable("id") long surveyId, Model model,
                                @ModelAttribute(value = "priorities") Answer answers) {

        SurveyEntity surveyEntity = surveyRepository.findById(surveyId).orElse(null);

        if (surveyEntity != null) {
            Map<String, String> answersMap = answers.getPriorities();

            for (Map.Entry<String, String> entry : answersMap.entrySet()) {
                String question = entry.getKey();
                String answer = entry.getValue();

                Long questionId = null;
                List<QuestionEntity> questions = surveyEntity.getQuestions();
                for (QuestionEntity questionEntity : questions) {
                    if (questionEntity.getQuestionText().equals(question)) {
                        questionId = questionEntity.getId();
                    }
                }

                QuestionEntity questionEntity = questionRepository.findById(questionId).orElse(null);
                Long answerId = null;
                List<AnswerEntity> questionAnswers = questionEntity.getAnswers();
                for (AnswerEntity answerEntity : questionAnswers) {
                    if (answerEntity.getAnswerText().equals(answer)) {
                        answerId = answerEntity.getId();
                    }
                }

                AnswerEntity answerEntity = answerRepository.findById(answerId).orElse(null);
                answerEntity.setVotes(answerEntity.getVotes() + 1);
                answerController.updateQuestionAnswer(answerEntity.getId(), answerEntity);
                model.addAttribute("message", "Uspješno ste ispunili anketu!");
            }
        } else {
            model.addAttribute("message", "Anketa sa danim ID-em ne postoji");
        }
        return "survey-completed";
    }

    @GetMapping("/survey/{id}/stats")
    public String getSurveyStats(@PathVariable Long id, Model model) {
        SurveyEntity survey = this.surveyRepository.findById(id).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = auth.getName();
        if(survey==null ) {
        	model.addAttribute("message", "Tražena anketa ne postoji");
        } else {
        	if(loggedInUser!=null && loggedInUser.equals(survey.getUser().getUsername())) {
        		model.addAttribute("survey", survey);
                model.addAttribute("questions", survey.getQuestions());
        	} else {
        		model.addAttribute("wrongUser", "Niste stvorili anketu, nemate pristup pregledu statistike!");
        	}
        }
        return "stats";
    }
    
    @GetMapping("/survey/{id}/{active}")
    public String setSurveyActivity(@PathVariable Long id, @PathVariable boolean active, Model model) {
    	SurveyEntity survey = this.surveyRepository.findById(id).orElse(null);
        if (survey == null) {
            return homepage(model);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = auth.getName();

        if (loggedInUser!=null && loggedInUser.equals(survey.getUser().getUsername())) {
            survey.setIsActive(active);
            this.surveyRepository.saveAndFlush(survey);
            return getUserOverview(model);
        }
        else {
            return  homepage(model);
        }
    }

}