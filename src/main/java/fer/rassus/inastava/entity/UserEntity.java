package fer.rassus.inastava.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity(name="user")
public class UserEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private String password;
	private String name;
	
	@OneToMany(cascade = CascadeType.DETACH,
			mappedBy="user")
	private List<SurveyEntity> surveys = new ArrayList<>();
	
	public UserEntity() {
		
	}

	public UserEntity(String username, String password, String name) {
		this.username=username;
		this.password=password;
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SurveyEntity> getSurveys() {
		return surveys;
	}

	public void setSurveys(List<SurveyEntity> surveys) {
		this.surveys = surveys;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
