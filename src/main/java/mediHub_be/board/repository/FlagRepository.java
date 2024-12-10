package mediHub_be.board.repository;

import mediHub_be.board.entity.Flag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlagRepository extends JpaRepository<Flag, Long> {

    Optional<Flag> findByFlagTypeAndFlagEntitySeq(String flagType, Long entitySeq);
    List<Flag> findAllByFlagEntitySeq(Long flagEntitySeq);

}