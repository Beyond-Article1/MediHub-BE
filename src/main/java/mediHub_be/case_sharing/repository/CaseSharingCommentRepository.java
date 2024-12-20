package mediHub_be.case_sharing.repository;

import mediHub_be.case_sharing.entity.CaseSharingComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseSharingCommentRepository extends JpaRepository<CaseSharingComment, Long> {
    List<CaseSharingComment> findByCaseSharing_CaseSharingSeqAndDeletedAtIsNull(Long caseSharingSeq);

    List<CaseSharingComment> findByCaseSharing_CaseSharingSeqAndCaseSharingBlockIdAndDeletedAtIsNull(Long caseSharingSeq, String blockId);
}

