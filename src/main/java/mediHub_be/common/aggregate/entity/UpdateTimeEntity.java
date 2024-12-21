package mediHub_be.common.aggregate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // Auditing 기능을 포함시킨다.
@Getter
public abstract class UpdateTimeEntity extends CreateTimeEntity{
    @LastModifiedDate
    @Column(insertable = true, name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now(); // insert 시점에 updated_at 필드 값을 설정
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now(); // update 시점에 updated_at 필드 값을 설정
    }
}
