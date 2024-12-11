package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpOpinionLocationDTO;
import mediHub_be.cp.entity.CpOpinionLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CpOpinionLocationRepository extends JpaRepository<CpOpinionLocation, Long> {

    List<CpOpinionLocation> findByCpVersionSeq(long cpVersionSeq);

    // 6일(금) 여기서 조회 오류남
    @Query("SELECT new mediHub_be.cp.dto.ResponseCpOpinionLocationDTO( " +
            "col.cpOpinionLocationSeq, " +
            "col.cpVersionSeq, " +
            "col.cpOpinionLocationX, " +
            "col.cpOpinionLocationY, " +
            "co.userSeq) " +
            "FROM CpOpinionLocation AS col " +
            "JOIN CpOpinion AS co ON col.cpOpinionLocationSeq = co.cpOpinionLocationSeq " +
            "WHERE col.cpOpinionLocationSeq = :cpOpinionLocationSeq")
    Optional<ResponseCpOpinionLocationDTO> findByCpOpinionLocation_CpOpinion_CpOpinionLocationSeq(@Param("cpOpinionLocationSeq") long cpOpinionLocationSeq);
}
