package mediHub_be.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "picture")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Picture extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pictureSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="flag_seq", nullable = false)
    private Flag flag;

    private String pictureUrl; // 이미지 URL
    private String pictureName; // 이미지 파일 이름
    private String pictureType; // 이미지 타입 (확장자)

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
