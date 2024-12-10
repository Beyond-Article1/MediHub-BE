package mediHub_be.anonymousBoard.repository;

import mediHub_be.anonymousBoard.entity.AnonymousBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnonymousBoardRepository extends JpaRepository<AnonymousBoard, Long> {

    List<AnonymousBoard> findAllByAnonymousBoardIsDeletedFalse();
}