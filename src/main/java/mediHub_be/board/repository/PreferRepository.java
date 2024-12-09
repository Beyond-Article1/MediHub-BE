package mediHub_be.board.repository;

import mediHub_be.board.entity.Prefer;
import mediHub_be.board.entity.Flag;
import mediHub_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferRepository extends JpaRepository<Prefer, Long> {

    Optional<Prefer> findByUserAndFlag(User user, Flag flag);
    boolean existsByUserAndFlag(User user, Flag flag);
}