package mediHub_be.board.entity;

import jakarta.persistence.*;
import lombok.*;
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
    private Long pictureSeq;            // 사진 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="flag_seq", nullable = false)
    private Flag flag;                  // 식별 번호

    private String pictureName;         // 파일 명
    private String pictureChangedName;  // 변경된 파일 명
    private String pictureUrl;          // URL
    private String pictureType;         // 타입
    private Boolean pictureIsDeleted;   // 상태

    // 생성일은 BaseCreateDeleteEntity에서 상속
    // 삭제일은 BaseCreateDeleteEntity에서 상속

    public void setDeleted() {

        this.pictureIsDeleted = true;

        delete();
    }
}