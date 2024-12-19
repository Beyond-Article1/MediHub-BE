package mediHub_be.board.repository;

import mediHub_be.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByFlag_FlagSeqAndCommentIsDeletedFalse(Long flagSeq);
    List<Comment> findAllByFlag_FlagSeqAndCommentIsDeletedFalse(Long flagSeq);
    Comment findByCommentSeqAndCommentIsDeletedFalse(Long commentSeq);
}