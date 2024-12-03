package mediHub_be.notify.repository;

import mediHub_be.notify.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<Notify, Long> {
}
