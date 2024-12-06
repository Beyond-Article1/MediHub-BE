package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpOpinionLocationDTO;
import mediHub_be.cp.entity.CpOpinionLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpOpinionLocationRepository extends JpaRepository<CpOpinionLocation, Long> {

    List<CpOpinionLocation> findByCpVersionSeq(long cpVersionSeq);

    ResponseCpOpinionLocationDTO findByCpOpinionLocation_CpOpinion_CpOpinionLocationSeq(long cpOpinionLocationSeq);
}
