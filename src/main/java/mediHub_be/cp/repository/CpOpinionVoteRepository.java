package mediHub_be.cp.repository;

import mediHub_be.cp.entity.CpOpinionVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpOpinionVoteRepository extends JpaRepository<CpOpinionVote, Long> {
}
