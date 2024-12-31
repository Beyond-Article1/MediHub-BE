package mediHub_be.cp.repository;

import mediHub_be.cp.entity.CpOpinionLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CpOpinionLocationRepository extends JpaRepository<CpOpinionLocation, Long> {

    // CP 버전 시퀀스를 사용하여 삭제되지 않은 CP 의견 위치를 조회하는 메서드
    List<CpOpinionLocation> findByCpVersionSeqAndDeletedAtIsNull(long cpVersionSeq);
}
