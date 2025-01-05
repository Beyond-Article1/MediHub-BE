package mediHub_be.board.repository;

import mediHub_be.board.dto.PreferDTO;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Prefer;
import mediHub_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PreferRepository extends JpaRepository<Prefer, Long> {

    Optional<Prefer> findByUserAndFlag(User user, Flag flag);
    boolean existsByUserAndFlag(User user, Flag flag);

    // 직원과 식별 번호로 찾기
    @Query("SELECT new mediHub_be.board.dto.PreferDTO(b.preferSeq, b.flag, b.createdAt)" +
            "  FROM Prefer b " +
            " WHERE b.user = :user " +
            "   AND b.flag.flagType = :flagType")
    List<PreferDTO> findByUserAndFlagType(User user, String flagType);

    @Transactional
    @Modifying
    @Query("DELETE FROM Prefer b WHERE b.flag.flagSeq = :flagSeq")
    void deleteAllByFlagSeq(Long flagSeq);


    @Query("SELECT COUNT(p) " +
            "FROM Prefer p " +
            "JOIN p.flag f " +
            "WHERE f.flagType = 'MEDICAL_LIFE' " +
            "AND p.user = :user " +
            "AND p.createdAt BETWEEN :startDate AND :endDate")
    Long countMedicalLifeLikesByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}