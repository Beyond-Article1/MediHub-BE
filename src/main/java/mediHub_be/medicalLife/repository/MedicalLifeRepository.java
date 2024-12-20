package mediHub_be.medicalLife.repository;

import mediHub_be.medicalLife.entity.MedicalLife;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalLifeRepository extends JpaRepository<MedicalLife, Long> {

    MedicalLife findByMedicalLifeSeqAndMedicalLifeIsDeletedFalse(Long medicalLifeSeq);

    List<MedicalLife> findAllByMedicalLifeIsDeletedFalse();
}
