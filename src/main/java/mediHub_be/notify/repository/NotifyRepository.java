package mediHub_be.notify.repository;

import mediHub_be.notify.entity.NotiReadStatus;
import mediHub_be.notify.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Long> {

    // userId로 안읽은 Notify들 찾기
    @Query("SELECT n FROM Notify n WHERE n.noti_isRead = 'N' AND n.receiver.userId = :userId")
    List<Notify> findNotReadNotifyByUserId(String userId);

    // userId로 모든 Notify들 찾기
    @Query("SELECT n FROM Notify n WHERE n.receiver.userId = :userId")
    List<Notify> findAllByUserId(String userId);

}
