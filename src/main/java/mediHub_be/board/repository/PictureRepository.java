package mediHub_be.board.repository;

import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    Optional<Picture> findByFlag_FlagSeq(Long flagSeq);

    List<Picture> findAllByFlag_FlagSeq(Long flagSeq);

    void deleteByFlag(Flag flag);

    Optional<Picture> findByPictureUrl(String url);

    List<Picture> findByFlagFlagTypeAndFlagFlagEntitySeq(String flagType, Long flagEntitySeq);


    Optional<Picture> findByFlag_FlagSeqAndDeletedAtIsNull(Long flagSeq); // deleted_at이 null인 조건 추가

    List<Picture> findAllByFlag_FlagSeqAndDeletedAtIsNull(Long flagSeq);

    @Query("SELECT p " +
            "FROM Picture AS p " +
            "JOIN Flag AS f ON f.flagSeq = p.flag.flagSeq " +
            "WHERE f.flagEntitySeq = :entitySeq " +
            "ORDER BY p.createdAt DESC " +
            "LIMIT 1")
    Optional<Picture> findUserProfile(@Param("userSeq") long userSeq);
}