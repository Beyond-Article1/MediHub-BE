package mediHub_be.case_sharing.repository;

import mediHub_be.case_sharing.entity.OpenScope;
import mediHub_be.case_sharing.entity.Template;
import mediHub_be.part.entity.Part;
import mediHub_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findByOpenScope(OpenScope openScope);
    List<Template> findByUser_UserSeq(Long userSeq);
    List<Template> findByPart_PartSeq(Long partSeq);

    List<Template> findByUser(User user);

    List<Template> findSharedTemplatesByPart(Part part);

    List<Template> findByPart_PartSeqAndOpenScope(long partSeq, OpenScope openScope);
}
