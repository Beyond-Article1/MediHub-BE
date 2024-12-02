package mediHub_be.common.aggregate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
// 생성, 삭제
public abstract class BaseCreateDeleteEntity extends CreateTimeEntity{
    @Column(name = "deleted_at")
    private LocalDateTime deleteAt;

    public void delete(){
        this.deleteAt = LocalDateTime.now();
    }
}
