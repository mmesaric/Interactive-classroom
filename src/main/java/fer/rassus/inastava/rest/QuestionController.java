package fer.rassus.inastava.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fer.rassus.inastava.entity.QuestionEntity;
import fer.rassus.inastava.entity.SurveyEntity;
import fer.rassus.inastava.jpa.QuestionRepository;
import fer.rassus.inastava.jpa.SurveyRepository;

@RestController
@RequestMapping("/questions")
public class QuestionController {
	
	@Autowired QuestionRepository questionRepository;
	@Autowired SurveyRepository surveyRepository;
	
	@GetMapping("/{survey_id}")
	public List<QuestionEntity> getSurveysQuestions(@PathVariable Long survey_id) {
		return questionRepository.getQuestionsBySurvey(survey_id);
	}
	
	@PutMapping("/{survey_id}")
	public QuestionEntity addSurveyQuestion(@PathVariable Long survey_id,
			@RequestBody QuestionEntity question){
		SurveyEntity survey = surveyRepository.findById(survey_id).get();
		if(survey == null) {
			return null;
		}
		question.setSurvey(survey);
		questionRepository.saveAndFlush(question);	
		survey.getQuestions().add(question);
		surveyRepository.saveAndFlush(survey);
		return question;
	}
	
	@PostMapping("/{id}")
	public QuestionEntity updateSurveyQuestion(@PathVariable Long id,
			@RequestBody QuestionEntity question){
		if(!questionRepository.existsById(id)) {
			return null;
		}
		question.setId(id);
		return questionRepository.saveAndFlush(question);
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteQuestion(@PathVariable Long id) {
		if(!questionRepository.existsById(id)) {
			return false;
		}
		questionRepository.deleteById(id);
		return !questionRepository.existsById(id);
	}

}
