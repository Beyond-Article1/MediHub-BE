package mediHub_be.cp.controller;

import mediHub_be.cp.entity.CpVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpVersionRepository extends JpaRepository<CpVersion, Long> {
}
