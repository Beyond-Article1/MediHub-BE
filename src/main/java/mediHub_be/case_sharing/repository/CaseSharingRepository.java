package mediHub_be.case_sharing.repository;

import io.micrometer.common.KeyValues;
import mediHub_be.case_sharing.entity.CaseSharing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CaseSharingRepository extends JpaRepository<CaseSharing, Long> {
    // 최신 버전의 케이스 공유 목록만 조회
    @Query("SELECT c FROM CaseSharing c WHERE c.caseSharingIsLatest = true AND c.caseSharingIsDraft = false AND c.deletedAt IS NULL")
    List<CaseSharing> findAllLatestVersionsNotDraftAndDeletedAtIsNull();

    // 최신 레코드 하나만 반환
    Optional<CaseSharing> findTopByCaseSharingGroup_CaseSharingGroupSeqAndCaseSharingSeqNotAndCaseSharingIsDraftFalseAndDeletedAtIsNullOrderByCreatedAtDesc(
            Long caseSharingGroupSeq, Long excludedCaseSharingSeq
    );



    // 최신 버전의 게시글 + 해당하는 part 목록 조회
    @Query("SELECT c FROM CaseSharing c WHERE c.part.partSeq = :partSeq AND c.caseSharingIsLatest = true AND c.caseSharingIsDraft = false AND c.deletedAt IS NULL")
    List<CaseSharing> findByPartPartSeqAndCaseSharingIsLatestTrueAndIsDraftFalseAndDeletedAtIsNull(Long partSeq);

    @Query("SELECT c FROM CaseSharing c WHERE c.caseSharingGroup.caseSharingGroupSeq = :caseSharingGroupSeq AND c.caseSharingIsDraft = false AND c.deletedAt IS NULL")
    List<CaseSharing> findByCaseSharingGroupAndIsDraftFalseAndDeletedAtIsNull(Long caseSharingGroupSeq);

    List<CaseSharing> findByUserUserSeqAndCaseSharingIsDraftTrueAndDeletedAtIsNull(Long userSeq);

    List<CaseSharing> findByUserUserSeqAndCaseSharingIsDraftFalseAndDeletedAtIsNullAndCaseSharingIsLatestIsTrue(Long userSeq);


    Optional<CaseSharing> findByCaseSharingSeqAndCaseSharingIsDraftTrueAndDeletedAtIsNull(Long caseSharingSeq);

    @Query("SELECT c FROM CaseSharing c " +
            "WHERE c.createdAt >= :oneWeekAgo " +
            "AND c.caseSharingIsDraft = false " +
            "AND c.deletedAt IS NULL " +
            "ORDER BY c.caseSharingViewCount DESC")
    List<CaseSharing> findTop3ByCreatedAtAfterOrderByCaseSharingViewCountDesc(
            @Param("oneWeekAgo") LocalDateTime oneWeekAgo,
            Pageable pageable);

}
