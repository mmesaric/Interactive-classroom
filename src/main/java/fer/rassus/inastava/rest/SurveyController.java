package fer.rassus.inastava.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fer.rassus.inastava.entity.SurveyEntity;
import fer.rassus.inastava.jpa.AnswerRepository;
import fer.rassus.inastava.jpa.QuestionRepository;
import fer.rassus.inastava.jpa.SurveyRepository;
import fer.rassus.inastava.jpa.UserRepository;

@Controller
@RequestMapping("/surveys")
public class SurveyController {
	
	@Autowired SurveyRepository surveyRepository;
	@Autowired UserRepository userRepository;
	@Autowired QuestionRepository questionRepository;
	@Autowired AnswerRepository answerRepository;
	
	@GetMapping("")
	public List<SurveyEntity> getSurveys(){
		return surveyRepository.findAll();
	}
	
	
	@GetMapping("/users/{user_id}")
	public List<SurveyEntity> getSurveysByUser(@PathVariable Long user_id) {
		return surveyRepository.getSurveysByUser(user_id);
	}
	
	@PostMapping("")
	public SurveyEntity addSurvey(@ModelAttribute SurveyEntity newSurvey) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		newSurvey.setUser(userRepository.findByUsername(username));
		return surveyRepository.saveAndFlush(newSurvey);
	}
	
	@GetMapping("/{id}")
	public SurveyEntity getSurvey(@PathVariable Long id) {
		if(!surveyRepository.existsById(id)) {
			return null;
		}
		return surveyRepository.findById(id).get();
	}
	
	@PostMapping("/{id}")
	public SurveyEntity updateSurvey(@PathVariable Long id, @RequestBody SurveyEntity updatedSurvey) {
		if(!surveyRepository.existsById(id)) {
			return null;
		}
		updatedSurvey.setId(id);
		return surveyRepository.saveAndFlush(updatedSurvey);
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteSurvey(@PathVariable Long id) {
		if(!surveyRepository.existsById(id)) {
			return false;
		}
		surveyRepository.deleteById(id);
		return !surveyRepository.existsById(id);
	}

}
