package mediHub_be.board.repository;

import mediHub_be.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByFlag_FlagSeqAndCommentIsDeletedFalse(Long flagSeq);
    List<Comment> findAllByFlag_FlagSeq(Long flagSeq);
}