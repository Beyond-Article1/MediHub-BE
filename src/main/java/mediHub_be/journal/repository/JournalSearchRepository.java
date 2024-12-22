package mediHub_be.journal.repository;

import mediHub_be.journal.dto.ResponseJournalLogDTO;
import mediHub_be.journal.dto.ResponseJournalRankDTO;
import mediHub_be.journal.entity.Journal;
import mediHub_be.journal.entity.JournalSearch;
import mediHub_be.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JournalSearchRepository extends JpaRepository<JournalSearch, Long> {

    @Query("SELECT js FROM JournalSearch js WHERE js.user = :user AND js.journal = :journal")
    Optional<JournalSearch> findByUserAndJournal(User user, Journal journal);

    // 조회수순으로 상위 100개 조회 및 카운트 수 입력
    @Query("SELECT new mediHub_be.journal.dto.ResponseJournalRankDTO(js.journal, COUNT(js)) " +
            "FROM JournalSearch js " +
            "GROUP BY js.journal " +
            "ORDER BY COUNT(js) DESC")
    Page<ResponseJournalRankDTO> findTopJournalsWithSearchCount(Pageable pageable);

    // 내가 조회한 논문 시간순으로 정렬
    @Query("SELECT new mediHub_be.journal.dto.ResponseJournalLogDTO(js.journal, js.createdAt) " +
            "FROM JournalSearch js " +
            "WHERE js.user = :user " +
            "ORDER BY js.createdAt DESC")
    List<ResponseJournalLogDTO> findJournalLogs(User user);

    // 북마크 순으로 상위 100개 조회 및 북마크 수 입력
    @Query("SELECT new mediHub_be.journal.dto.ResponseJournalRankDTO(js.journal, bm.bookmarkCount) " +
            "FROM JournalSearch js " +
            "LEFT JOIN (SELECT b.flag.flagEntitySeq AS journalSeq, COUNT(b) AS bookmarkCount " +
            "           FROM Bookmark b " +
            "           WHERE b.flag.flagType = 'JOURNAL' " +
            "           GROUP BY b.flag.flagEntitySeq) bm " +
            "ON bm.journalSeq = js.journal.journalSeq " +
            "GROUP BY js.journal, bm.bookmarkCount " +
            "ORDER BY bm.bookmarkCount DESC")
    Page<ResponseJournalRankDTO> findTopJournalsWithBookmarkCount(Pageable pageable);

}
