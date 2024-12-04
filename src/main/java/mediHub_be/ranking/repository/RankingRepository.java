package mediHub_be.ranking.repository;

import mediHub_be.ranking.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
}