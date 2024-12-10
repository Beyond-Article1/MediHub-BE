package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "cp")
@Getter
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

    public void increaseCpViewCount() {
        this.cpViewCount++;
    }
}
