package mediHub_be.board.repository;

import mediHub_be.board.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    @Query("SELECT k FROM Keyword k " +
            "WHERE k.flagSeq IN (" +
            "   SELECT f.flagSeq FROM Flag f " +
            "   WHERE f.flagBoardFlag = :boardFlag AND f.flagPostSeq = :postSeq" +
            ")")
    List<Keyword> findByBoardFlagAndPostSeq(@Param("boardFlag") String boardFlag,
                                            @Param("postSeq") Long postSeq);

    void deleteByBoardFlagAndPostSeq(String boardFlag, Long postSeq);
}

