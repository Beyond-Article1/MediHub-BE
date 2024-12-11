package mediHub_be.cp.repository;

import mediHub_be.cp.entity.CpOpinionVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CpOpinionVoteRepository extends JpaRepository<CpOpinionVote, Long> {
    List<CpOpinionVote> findByCpOpinionSeq(long cpOpinionSeq);
}
