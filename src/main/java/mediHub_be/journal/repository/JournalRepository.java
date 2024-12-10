package mediHub_be.journal.repository;

import mediHub_be.journal.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JournalRepository extends JpaRepository<Journal, Long> {

    @Query("SELECT j FROM Journal j WHERE j.journalPmid = :journalPmid")
    Optional<Journal> findByJournalPmid(String journalPmid);
}
