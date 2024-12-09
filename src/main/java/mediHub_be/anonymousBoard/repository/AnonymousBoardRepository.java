package mediHub_be.anonymousBoard.repository;

import mediHub_be.anonymousBoard.entity.AnonymousBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnonymousBoardRepository extends JpaRepository<AnonymousBoard, Long> {

    List<AnonymousBoard> findAllByAnonymousBoardIsDeletedFalse();
}