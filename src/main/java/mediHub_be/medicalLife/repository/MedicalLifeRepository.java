package mediHub_be.medicalLife.repository;

import mediHub_be.medicalLife.entity.MedicalLife;
import mediHub_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MedicalLifeRepository extends JpaRepository<MedicalLife, Long> {

    MedicalLife findByMedicalLifeSeqAndMedicalLifeIsDeletedFalse(Long medicalLifeSeq);

    List<MedicalLife> findAllByMedicalLifeIsDeletedFalse();

    List<MedicalLife> findByUser_UserSeqAndMedicalLifeIsDeletedFalse(Long userSeq);

    List<MedicalLife> findTop3ByMedicalLifeIsDeletedFalseOrderByMedicalLifeViewCountDesc();

    List<MedicalLife> findAllByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

}
