package mediHub_be.medical_life.repository;

import mediHub_be.medical_life.entity.MedicalLife;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MedicalLifeRepository extends JpaRepository<MedicalLife, Long> {

    MedicalLife findMedicalLifeSeqAndMedicalLifeIsDEletedFalse(Long medicalLifeSeq);
    List<MedicalLife> findAllByMedicalLifeIsDeletedFalse();
}
