package mediHub_be.dept.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "dept")
@EntityListeners(AuditingEntityListener.class)
public class Dept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_seq", nullable = false)
    private Long deptSeq;

    @Column(name = "dept_name", nullable = false)
    private String deptName;


    @Builder
    public Dept(String deptName) {
        this.deptName = deptName;
    }

    public void updateDept(String deptName) {
        this.deptName = deptName;
    }
}
