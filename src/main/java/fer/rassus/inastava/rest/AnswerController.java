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

import fer.rassus.inastava.entity.AnswerEntity;
import fer.rassus.inastava.entity.QuestionEntity;
import fer.rassus.inastava.jpa.AnswerRepository;
import fer.rassus.inastava.jpa.QuestionRepository;

@RestController
@RequestMapping("/answers")
public class AnswerController {
	
	@Autowired QuestionRepository questionRepository;
	@Autowired AnswerRepository answerRepository;
	
	@GetMapping("/{question_id}")
	public List<AnswerEntity> getQuestionsAnswers(@PathVariable Long question_id) {
		return answerRepository.getAnswersByQuestions(question_id);
	}
	
	@PutMapping("/{question_id}")
	public AnswerEntity addSurveyQuestion(@PathVariable Long question_id,
			@RequestBody AnswerEntity answer){
		QuestionEntity question = questionRepository.findById(question_id).get();
		if(question == null) {
			return null;
		}
		answer.setQuestion(question);
		answerRepository.saveAndFlush(answer);	
		question.getAnswers().add(answer);
		questionRepository.saveAndFlush(question);
		return answer;
	}
	
	@PostMapping("/{id}")
	public AnswerEntity updateQuestionAnswer(@PathVariable Long id,
			@RequestBody AnswerEntity answer){
		if(!answerRepository.existsById(id)) {
			return null;
		}
		answer.setId(id);
		return answerRepository.saveAndFlush(answer);
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteAnswer(@PathVariable Long id) {
		if(!answerRepository.existsById(id)) {
			return false;
		}
		answerRepository.deleteById(id);
		return !answerRepository.existsById(id);
	}

}
