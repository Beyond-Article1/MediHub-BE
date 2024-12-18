package mediHub_be.part.repository;

import mediHub_be.part.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> {
    List<Part> findByDept_DeptSeqOrderByPartName(Long deptSeq);
    
    List<Part> findByDeletedAtIsNull();
}
