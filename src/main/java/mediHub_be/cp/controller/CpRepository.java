package mediHub_be.cp.controller;

import mediHub_be.cp.entity.Cp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRepository extends JpaRepository<Cp, Long> {
}
