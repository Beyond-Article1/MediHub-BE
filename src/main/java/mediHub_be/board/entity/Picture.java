package mediHub_be.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.BaseCreateDeleteEntity;

@Entity
@Table(name = "picture")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Picture extends BaseCreateDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pictureSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="flag_seq", nullable = false)
    private Flag flag;

    private String pictureName;         // 이미지 파일 이름
    private String pictureChangedName;  // 수정된 이미지 파일 이름
    private String pictureUrl;          // 이미지 URL
    private String pictureType;         // 이미지 타입 (확장자)
    private Boolean pictureIsDeleted;   // 이미지 상태

    // 작성일은 BaseFullEntity에서 상속
    // 삭제일은 BaseFullEntity에서 상속

    public void setDeleted() {

        this.pictureIsDeleted = true;

        delete();
    }
}