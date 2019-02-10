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

import fer.rassus.inastava.entity.UserEntity;
import fer.rassus.inastava.jpa.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired UserRepository userRepository;
	
	@GetMapping("")
	public List<UserEntity> getUsers(){
		return userRepository.findAll();
	}
	
	@PutMapping("")
	public UserEntity addUser(@RequestBody UserEntity newUser) {
		return userRepository.saveAndFlush(newUser);
	}
	
	@GetMapping("/{id}")
	public UserEntity getUser(@PathVariable Long id) {
		if(!userRepository.existsById(id)) {
			return null;
		}
		return userRepository.findById(id).get();
	}
	
	@PostMapping("/{id}")
	public UserEntity getUser(@PathVariable Long id, @RequestBody UserEntity updatedUser) {
		if(!userRepository.existsById(id)) {
			return null;
		}
		updatedUser.setId(id);
		return userRepository.saveAndFlush(updatedUser);
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteUser(@PathVariable Long id) {
		if(!userRepository.existsById(id)) {
			return false;
		}
		userRepository.deleteById(id);
		return !userRepository.existsById(id);
	}
	
}
