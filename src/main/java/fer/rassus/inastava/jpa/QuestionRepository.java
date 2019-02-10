package fer.rassus.inastava.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fer.rassus.inastava.entity.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
	
	@Query(value = "select * from question q where q.survey_id = ?1 ",
			nativeQuery = true)
	public List<QuestionEntity> getQuestionsBySurvey(Long id);

}
