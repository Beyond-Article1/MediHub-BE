package mediHub_be.follow.repository;

import mediHub_be.follow.dto.ResponseFollowingsDTO;
import mediHub_be.follow.entity.Follow;
import mediHub_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface FollowRepository extends JpaRepository<Follow, Long> {

    // fromUser와 toUser로 팔로우 목록에서 찾기
    Optional<Follow> findByUserFromAndUserTo(User fromUser, User toUser);

    // 자신의 팔로잉 목록 확인
    @Query("SELECT new mediHub_be.follow.dto.ResponseFollowingsDTO(f.followSeq, f.userTo) " +
            "FROM Follow f WHERE f.userFrom = :fromUser")
    List<ResponseFollowingsDTO> findByFollowings(User fromUser);

    // 자신의 팔로워들 확인
    @Query("SELECT f.userFrom FROM Follow f WHERE f.userTo = :toUser")
    List<User> findUserFromByToUser(User toUser);

    // 자신의 팔로워들 몇명인지 확인
    @Query("SELECT COUNT(*) FROM Follow f WHERE f.userTo = :toUser")
    Long countByToUser(User toUser);

    @Query("SELECT f.userFrom FROM Follow f WHERE f.userTo = :user")
    List<User> findFollowersByUser(User user);
}
