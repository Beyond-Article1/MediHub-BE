package mediHub_be.board.repository;

import mediHub_be.board.dto.BookmarkDTO;
import mediHub_be.board.entity.Bookmark;
import mediHub_be.board.entity.Flag;
import mediHub_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndFlag(User user, Flag flag);

    boolean existsByUserAndFlag(User user, Flag flag);

    // 게시판 식별 타입과 유저로 찾기
    @Query("SELECT new mediHub_be.board.dto.BookmarkDTO(b.bookmarkSeq, b.flag, b.createdAt)" +
            " FROM Bookmark b " +
            "WHERE b.user = :user " +
            "AND b.flag.flagType = :flagType")
    List<BookmarkDTO> findByUserAndFlagType(User user, String flagType);

    @Transactional
    @Modifying
    @Query("DELETE FROM Bookmark b WHERE b.flag.flagSeq = :flagSeq")
    void deleteAllByFlagSeq(Long flagSeq);
}
