package mediHub_be.rank.repository;

import mediHub_be.rank.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Long> {
}