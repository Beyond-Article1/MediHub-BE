package mediHub_be.user.repository;




import mediHub_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 ID로 사용자 정보 조회
    Optional<User> findByUserId(String userId);

}
