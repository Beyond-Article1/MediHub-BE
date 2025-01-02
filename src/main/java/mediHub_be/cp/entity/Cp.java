package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cp")
@Getter
@ToString
@NoArgsConstructor
public class Cp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpSeq;             // cp 번호

    @Column
    private long userSeq;           // 등록인

    @Column
    private String cpName;          // cp명

    @Column
    private String cpDescription;   // cp 설명

    @Column
    private long cpViewCount;       // cp 조회수

    @Builder
    public Cp(
            long cpSeq,
            long userSeq,
            String cpName,
            String cpDescription,
            long cpViewCount) {
        this.cpSeq = cpSeq;
        this.userSeq = userSeq;
        this.cpName = cpName;
        this.cpDescription = cpDescription;
        this.cpViewCount = cpViewCount;
    }

    public void increaseCpViewCount() {
        this.cpViewCount++;
    }
}
