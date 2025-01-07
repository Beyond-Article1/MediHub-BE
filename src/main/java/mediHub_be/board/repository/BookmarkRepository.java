package mediHub_be.board.repository;

import mediHub_be.board.dto.BookmarkDTO;
import mediHub_be.board.entity.Bookmark;
import mediHub_be.board.entity.Flag;
import mediHub_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserAndFlag(User user, Flag flag);
    boolean existsByUserAndFlag(User user, Flag flag);

    // 구분자와 엔터티 번호로 찾기
    @Query("SELECT new mediHub_be.board.dto.BookmarkDTO(b.bookmarkSeq, b.flag, b.createdAt)" +
           "  FROM Bookmark b " +
           " WHERE b.user = :user " +
           "   AND b.flag.flagType = :flagType"
    )
    List<BookmarkDTO> findByUserAndFlagType(User user, String flagType);

    @Transactional
    @Modifying
    @Query("DELETE FROM Bookmark b WHERE b.flag.flagSeq = :flagSeq")
    void deleteAllByFlagSeq(Long flagSeq);

    // 특정 caseSharing을 북마크한 모든 사용자 조회
    List<Bookmark> findByFlag_FlagTypeAndFlag_FlagEntitySeq(String flagType, Long flagEntitySeq);

    @Query("SELECT COUNT(b) " +
            "FROM Bookmark b " +
            "JOIN b.flag f " +
            "JOIN CaseSharing cs ON cs.caseSharingSeq = f.flagEntitySeq " + // 논리적 연결
            "WHERE f.flagType = 'CASE_SHARRING' " +
            "AND b.user = :user " +
            "AND cs.user = :user " + // CaseSharing의 작성자 기준
            "AND b.createdAt BETWEEN :startDate AND :endDate")
    Long countCaseSharingBookmarksByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(b) " +
            "FROM Bookmark b " +
            "JOIN b.flag f " +
            "JOIN CaseSharing cs ON cs.caseSharingSeq = f.flagEntitySeq " + // 논리적 연결
            "WHERE f.flagType = 'MEDICAL_LIFE' " +
            "AND cs.user = :user " + // CaseSharing의 작성자 기준
            "AND b.createdAt BETWEEN :startDate AND :endDate")
    Long countMedicalLifeBookmarksByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    Long countByFlag_FlagEntitySeqInAndFlag_FlagType(List<Long> caseSharingIds, String caseSharring);
}