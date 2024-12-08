package mediHub_be.board.repository;

import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    Optional<Picture> findByFlag_FlagSeq(Long flagSeq);

    void deleteByFlag(Flag flag);
}
