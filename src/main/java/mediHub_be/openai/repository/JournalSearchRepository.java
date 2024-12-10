package mediHub_be.openai.repository;

import mediHub_be.openai.entity.Journal;
import mediHub_be.openai.entity.JournalSearch;
import mediHub_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JournalSearchRepository extends JpaRepository<JournalSearch, Long> {

    @Query("SELECT js FROM JournalSearch js WHERE js.user = :user AND js.journal = :journal")
    Optional<JournalSearch> findByUserAndJournal(User user, Journal journal);
}
