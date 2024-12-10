package mediHub_be.journal.repository;

import mediHub_be.journal.dto.ResponseJournalSearchDTO;
import mediHub_be.journal.entity.Journal;
import mediHub_be.journal.entity.JournalSearch;
import mediHub_be.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JournalSearchRepository extends JpaRepository<JournalSearch, Long> {

    @Query("SELECT js FROM JournalSearch js WHERE js.user = :user AND js.journal = :journal")
    Optional<JournalSearch> findByUserAndJournal(User user, Journal journal);

    // 조회수순으로 상위 100개 조회 및 카운트 수 입력
    @Query("SELECT new mediHub_be.journal.dto.ResponseJournalSearchDTO(js.journal, COUNT(js)) " +
            "FROM JournalSearch js " +
            "GROUP BY js.journal " +
            "ORDER BY COUNT(js) DESC")
    Page<ResponseJournalSearchDTO> findTopJournalsWithSearchCount(Pageable pageable);
}
