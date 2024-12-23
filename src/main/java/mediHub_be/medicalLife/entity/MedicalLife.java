package mediHub_be.medicalLife.entity;

import jakarta.persistence.*;
import lombok.*;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.user.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "medical_life")
@AllArgsConstructor
@Builder
public class MedicalLife extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_life_seq", nullable = false)
    private Long medicalLifeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Column(name = "medical_life_title", nullable = false, length = 255)
    private String medicalLifeTitle;

    @Column(name = "medical_life_content", nullable = false, columnDefinition = "LONGTEXT")
    private String medicalLifeContent;

    @Column(name = "medical_life_is_deleted", nullable = false)
    private Boolean medicalLifeIsDeleted = false;

    @Column(name = "medical_life_view_count", nullable = false)
    private Long medicalLifeViewCount = 0L;

    public void update(String medicalLifeTitle, String medicalLifeContent) {
        this.medicalLifeTitle= medicalLifeTitle;
        this.medicalLifeContent= medicalLifeContent;
    }

    public void increaseViewCount() {
        if(this.medicalLifeViewCount == null) {
            this.medicalLifeViewCount = 1L;
        } else this.medicalLifeViewCount++;
    }

    public void setDeleted() {
        this.deletedAt = LocalDateTime.now();
        this.medicalLifeIsDeleted = true;

    }
}
