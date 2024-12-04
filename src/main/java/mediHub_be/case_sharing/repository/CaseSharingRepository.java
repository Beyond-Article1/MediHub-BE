package mediHub_be.case_sharing.repository;

import mediHub_be.case_sharing.entity.CaseSharing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CaseSharingRepository extends JpaRepository<CaseSharing, Long> {
    // 최신 버전의 게시글 목록만 조회
    @Query("SELECT v.caseSharing FROM Version v WHERE v.versionIsLatest = true")
    List<CaseSharing> findAllLatestVersions();
}
