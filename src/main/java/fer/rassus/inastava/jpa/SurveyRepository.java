package fer.rassus.inastava.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fer.rassus.inastava.entity.SurveyEntity;
import org.springframework.data.repository.query.Param;

public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {
	
	@Query(value = "select * from surveys s where s.user = ?1 ", nativeQuery = true)
	List<SurveyEntity> getSurveysByUser(Long id);

}
