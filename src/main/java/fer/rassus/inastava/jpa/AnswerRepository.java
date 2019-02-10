package fer.rassus.inastava.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fer.rassus.inastava.entity.AnswerEntity;

public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
	
	@Query(value = "select * from answer a where a.question_id = :id ", 
			nativeQuery = true)
	List<AnswerEntity> getAnswersByQuestions(Long id);

	/*@Query(value = "update ")
	void updateVotes(Long id);*/

}
