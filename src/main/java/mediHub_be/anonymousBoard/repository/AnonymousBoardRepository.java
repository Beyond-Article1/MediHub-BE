package mediHub_be.anonymousBoard.repository;

import mediHub_be.anonymousBoard.entity.AnonymousBoard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AnonymousBoardRepository extends JpaRepository<AnonymousBoard, Long> {

    List<AnonymousBoard> findAllByAnonymousBoardIsDeletedFalse();
    AnonymousBoard findByAnonymousBoardSeqAndAnonymousBoardIsDeletedFalse(Long anonymousBoardSeq);
    List<AnonymousBoard> findByUser_UserSeqAndAnonymousBoardIsDeletedFalse(Long userSeq);

    @Query("SELECT a FROM AnonymousBoard a " +
            "WHERE a.createdAt >= :oneWeekAgo " +
            "AND a.deletedAt IS NULL " +
            "ORDER BY a.anonymousBoardViewCount DESC")
    List<AnonymousBoard> findTop3ByCreatedAtAfterOrderByAnonymousBoardViewCountDesc(
            @Param("oneWeekAgo") LocalDateTime oneWeekAgo,
            Pageable pageable);
}