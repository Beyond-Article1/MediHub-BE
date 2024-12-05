package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpOpinionDTO;
import mediHub_be.cp.entity.CpOpinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
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
            "u.part.partName) " +
            "FROM CpOpinion AS co " +
            "JOIN User AS u ON u.userSeq = co.userSeq " +
            "WHERE co.cpOpinionSeq = :cpOpinionSeq " +
            "AND co.deletedAt IS NULL")
    ResponseCpOpinionDTO findByCpOpinionSeqAndDeletedAtIsNull(long cpOpinionSeq);

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
            "u.part.partName) " +
            "FROM CpOpinion AS co " +
            "JOIN User AS u ON u.userSeq = co.userSeq " +
            "WHERE co.cpOpinionSeq = :cpOpinionSeq " +
            "AND co.deletedAt IS NOT NULL")
    ResponseCpOpinionDTO findByCpOpinionSeqAndDeletedAtIsNotNull(long cpOpinionSeq);
}
