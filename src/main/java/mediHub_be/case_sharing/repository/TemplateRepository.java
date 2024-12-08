package mediHub_be.case_sharing.repository;

import java.util.List;
import java.util.Optional;

import mediHub_be.case_sharing.entity.OpenScope;
import mediHub_be.case_sharing.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findByDeletedAtIsNull(); // 삭제되지 않은 템플릿 조회

    List<Template> findByUser_UserSeqAndDeletedAtIsNull(Long userSeq); // 내가 작성한 템플릿 (삭제되지 않은 것만)

    List<Template> findByPart_PartSeqAndOpenScopeAndDeletedAtIsNull(Long partSeq, OpenScope openScope); // 과 공유 템플릿

    List<Template> findByOpenScopeAndDeletedAtIsNull(OpenScope openScope); // 전체 공개 템플릿

    Optional<Template> findByTemplateSeqAndDeletedAtIsNull(Long templateSeq); // 삭제되지 않은 템플릿 상세 조회
}
