package mediHub_be.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.BaseFullEntity;

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

    private Long userSeq; // 작성자 ID
    private Long flagSeq; // 게시판 식별 플래그
    private String pictureUrl; // 이미지 URL
    private String pictureName; // 이미지 파일 이름
    private String pictureType; // 이미지 타입 (확장자)
}
