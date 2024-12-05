package mediHub_be.case_sharing.repository;

import mediHub_be.case_sharing.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
}
