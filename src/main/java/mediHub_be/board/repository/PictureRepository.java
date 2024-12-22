package mediHub_be.board.repository;

import mediHub_be.board.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    Optional<Picture> findByFlag_FlagSeq(Long flagSeq);
    List<Picture> findAllByFlag_FlagSeq(Long flagSeq);
    List<Picture> findByFlagFlagTypeAndFlagFlagEntitySeq(String flagType, Long flagEntitySeq);
    List<Picture> findByFlagFlagTypeAndFlagFlagEntitySeqAndPictureIsDeletedIsNotNull(String flagType, Long flagEntitySeq);
    Optional<Picture> findByFlag_FlagSeqAndDeletedAtIsNull(Long flagSeq);
    Optional<Picture> findFirstByFlag_FlagSeqOrderByCreatedAtDesc(Long flagSeq);
    List<Picture> findAllByFlag_FlagSeqAndPictureIsDeletedFalse(Long flagSeq);
}