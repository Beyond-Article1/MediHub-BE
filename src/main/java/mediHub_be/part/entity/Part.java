package mediHub_be.part.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.dept.entity.Dept;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "part")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE part SET deleted_at = LOCALTIME WHERE part_seq = ?")
public class Part extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_seq", nullable = false)
    private long partSeq;

    @ManyToOne
    @JoinColumn(name = "dept_seq", nullable = false)
    private Dept dept;

    @Column(name = "part_name", nullable = false)
    private String partName;

    @Builder
    public Part(Dept dept, String partName) {
        this.dept = dept;
        this.partName = partName;
    }

    public void updatePart(Dept dept, String partName) {
        this.dept = dept;
        this.partName = partName;
    }
}
