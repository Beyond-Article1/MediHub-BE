package mediHub_be.medical_life.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.user.entity.User;
import mediHub_be.dept.entity.Dept;
import mediHub_be.part.entity.Part;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "medical_life")
public class MedicalLife extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_life_seq", nullable = false)
    private Long medicalLifeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_seq", nullable = false)
    private Dept dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_seq", nullable = false)
    private Part part;

    @Column(name = "medical_life_title", nullable = false, length = 255)
    private String medicalLifeTitle;

    @Column(name = "medical_life_content", nullable = false, columnDefinition = "LONGTEXT")
    private String medicalLifeContent;

    @Column(name = "medical_life_is_deleted", nullable = false)
    private Boolean medicalLifeIsDeleted = false;

    @Column(name = "medical_life_view_count", nullable = false)
    private Long medicalLifeViewCount = 0L;

    @Builder
    public MedicalLife(User user, Dept dept, Part part, String medicalLifeTitle, String medicalLifeContent,
                       Boolean medicalLifeIsDeleted, Long medicalLifeViewCount) {
        this.user = user;
        this.dept = dept;
        this.part = part;
        this.medicalLifeTitle = medicalLifeTitle;
        this.medicalLifeContent = medicalLifeContent;
        this.medicalLifeIsDeleted = medicalLifeIsDeleted != null ? medicalLifeIsDeleted : false;
        this.medicalLifeViewCount = medicalLifeViewCount != null ? medicalLifeViewCount : 0L;
    }

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
