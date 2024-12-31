package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpOpinionDTO;
import mediHub_be.cp.entity.CpOpinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CpOpinionRepository extends JpaRepository<CpOpinion, Long> {

    // 활성 상태의 CP 의견을 조회하는 메서드
    @Query("SELECT new mediHub_be.cp.dto.ResponseCpOpinionDTO(" +
            "co.cpOpinionSeq, " +
            "co.cpOpinionContent, " +
            "co.createdAt, " +
            "co.updatedAt, " +
            "co.deletedAt, " +
            "co.cpOpinionViewCount, " +
            "u.userName, " +
            "u.userId, " +
            "u.userSeq, " +
            "u.part.partName) " +
            "FROM CpOpinion AS co " +
            "JOIN User AS u ON u.userSeq = co.userSeq " +
            "WHERE co.cpOpinionLocationSeq = :cpOpinionLocationSeq " +
            "AND co.deletedAt IS NULL")
    List<ResponseCpOpinionDTO> findByCpOpinionLocationSeqAndDeletedAtIsNull(long cpOpinionLocationSeq);

    // 삭제된 CP 의견을 조회하는 메서드
    @Query("SELECT new mediHub_be.cp.dto.ResponseCpOpinionDTO(" +
            "co.cpOpinionSeq, " +
            "co.cpOpinionContent, " +
            "co.createdAt, " +
            "co.updatedAt, " +
            "co.deletedAt, " +
            "co.cpOpinionViewCount, " +
            "u.userName, " +
            "u.userId, " +
            "u.userSeq, " +
            "u.part.partName) " +
            "FROM CpOpinion AS co " +
            "JOIN User AS u ON u.userSeq = co.userSeq " +
            "WHERE co.cpOpinionLocationSeq = :cpOpinionLocationSeq " +
            "AND co.deletedAt IS NOT NULL")
    List<ResponseCpOpinionDTO> findByCpOpinionLocationSeqAndDeletedAtIsNotNull(long cpOpinionLocationSeq);

    // 의견 번호로 의견을 조회하는 메서드
    @Query("SELECT new mediHub_be.cp.dto.ResponseCpOpinionDTO(" +
            "co.cpOpinionSeq, " +
            "co.cpOpinionContent, " +
            "co.createdAt, " +
            "co.updatedAt, " +
            "co.deletedAt, " +
            "co.cpOpinionViewCount, " +
            "u.userName, " +
            "u.userId, " +
            "u.userSeq, " +
            "u.part.partName) " +
            "FROM CpOpinion AS co " +
            "JOIN User AS u ON u.userSeq = co.userSeq " +
            "WHERE co.cpOpinionSeq = :cpOpinionSeq")
    Optional<ResponseCpOpinionDTO> findByCpOpinionSeq(@Param("cpOpinionSeq") long cpOpinionSeq);

    // 사용자가 작성한 CP 의견 조회 메서드
    @Query("SELECT new mediHub_be.cp.dto.ResponseCpOpinionDTO( " +
            "co.cpOpinionSeq, " +
            "co.cpOpinionContent, " +
            "co.createdAt, " +
            "co.updatedAt, " +
            "co.deletedAt, " +
            "co.cpOpinionViewCount, " +
            "u.userName, " +
            "u.userId, " +
            "u.userSeq, " +
            "u.part.partName) " +
            "FROM CpOpinion AS co " +
            "JOIN User AS u ON co.userSeq = u.userSeq " +
            "WHERE co.userSeq = :userSeq " +
            "AND co.deletedAt IS NULL")
    List<ResponseCpOpinionDTO> findByUserSeq(@Param("userSeq") long userSeq);
}
