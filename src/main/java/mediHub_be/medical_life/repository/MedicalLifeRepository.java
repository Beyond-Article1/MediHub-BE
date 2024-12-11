package mediHub_be.medical_life.repository;

import mediHub_be.medical_life.entity.MedicalLife;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicalLifeRepository extends JpaRepository<MedicalLife, Long> {

    @Query("SELECT m FROM MedicalLife m " +
            "JOIN m.dept d " +
            "JOIN m.part p " +
            "WHERE (:deptSeq IS NULL OR d.deptSeq = :deptSeq) " +
            "AND (:partSeq IS NULL OR p.partSeq = :partSeq) " +
            "AND m.medicalLifeIsDeleted = false")
    List<MedicalLife> findAllByDeptAndPart(@Param("deptSeq") Long deptSeq, @Param("partSeq") Long partSeq);

}
