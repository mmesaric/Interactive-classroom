package fer.rassus.inastava.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import fer.rassus.inastava.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //@Query(value = "SELECT * FROM user a WHERE a.username = :username ", nativeQuery = true)
    UserEntity findByUsername(String username);
}
