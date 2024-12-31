package mediHub_be.ranking.repository;

import mediHub_be.ranking.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    List<Ranking> findByDeptSeq(Long deptSeq);
}