package mediHub_be.cp.repository;

import mediHub_be.cp.entity.CpOpinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpOpinionRepository extends JpaRepository<CpOpinion, Long> {
}
