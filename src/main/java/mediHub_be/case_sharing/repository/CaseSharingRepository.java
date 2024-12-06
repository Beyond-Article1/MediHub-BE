package mediHub_be.case_sharing.repository;

import mediHub_be.case_sharing.entity.CaseSharing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaseSharingRepository extends JpaRepository<CaseSharing, Long> {
    // 최신 버전의 케이스 공유 목록만 조회
    @Query("SELECT c FROM CaseSharing c WHERE c.caseSharingIsLatest = true AND c.caseSharingIsDraft = false AND c.deletedAt IS NULL")
    List<CaseSharing> findAllLatestVersionsNotDraft();

    // 삭제되지 않은 게시글만 조회
    @Query("SELECT cs FROM CaseSharing cs WHERE cs.deletedAt IS NULL")
    List<CaseSharing> findAllActive();

    // 최신 이전 버전을 찾는 메서드
    @Query("SELECT c FROM CaseSharing c WHERE c.caseSharingGroup.caseSharingGroupSeq = :caseSharingGroupSeq " +
            "AND c.caseSharingSeq != :excludedCaseSharingSeq " +
            "AND c.caseSharingIsDraft = false AND c.deletedAt IS NULL " +
            "ORDER BY c.createdAt DESC")
    Optional<CaseSharing> findTopByCaseSharingGroupCaseSharingGroupSeqAndCaseSharingSeqNotAndIsDraftFalseAndDeletedAtIsNullOrderByCreatedAtDesc(
            @Param("caseSharingGroupSeq") Long caseSharingGroupSeq,
            @Param("excludedCaseSharingSeq") Long excludedCaseSharingSeq
    );

    // 최신 버전의 게시글 + 해당하는 part 목록 조회
    @Query("SELECT c FROM CaseSharing c WHERE c.part.partSeq = :partSeq AND c.caseSharingIsLatest = true AND c.caseSharingIsDraft = false AND c.deletedAt IS NULL")
    List<CaseSharing> findByPartPartSeqAndCaseSharingIsLatestTrueAndIsDraftFalseAndDeletedAtIsNull(Long partSeq);

    @Query("SELECT c FROM CaseSharing c WHERE c.caseSharingGroup.caseSharingGroupSeq = :caseSharingGroupSeq AND c.caseSharingIsDraft = false AND c.deletedAt IS NULL")
    List<CaseSharing> findByCaseSharingGroupAndIsDraftFalseAndDeletedAtIsNull(Long caseSharingGroupSeq);

    List<CaseSharing> findByUserUserSeqAndCaseSharingIsDraftTrue(Long userSeq);

    Optional<CaseSharing> findByCaseSharingSeqAndCaseSharingIsDraftTrue(Long caseSharingSeq);

}
