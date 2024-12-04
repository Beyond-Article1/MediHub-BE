package mediHub_be.case_sharing.repository;

import mediHub_be.case_sharing.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionRepository extends JpaRepository<Version, Long> {
    // 특정 케이스 공유 글에 속한 모든 버전 조회
    List<Version> findByCaseSharingCaseSharingSeq(Long caseSharingSeq);
}
