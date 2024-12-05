package mediHub_be.case_sharing.repository;

import mediHub_be.case_sharing.entity.CaseSharing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CaseSharingRepository extends JpaRepository<CaseSharing, Long> {
    // 최신 버전의 게시글 목록만 조회
    @Query("SELECT cs FROM CaseSharing cs WHERE cs.caseSharingIsLatest = true AND cs.deletedAt IS NULL")
    List<CaseSharing> findAllLatestVersions();

    // 삭제되지 않은 게시글만 조회
    @Query("SELECT cs FROM CaseSharing cs WHERE cs.deletedAt IS NULL")
    List<CaseSharing> findAllActive();
    // 최신 이전 버전을 찾는 메서드

    Optional<CaseSharing> findTopByCaseSharingGroupCaseSharingGroupSeqAndCaseSharingSeqNotAndDeletedAtIsNullOrderByCreatedAtDesc(
            Long caseSharingGroupSeq, Long excludedCaseSharingSeq);

}
