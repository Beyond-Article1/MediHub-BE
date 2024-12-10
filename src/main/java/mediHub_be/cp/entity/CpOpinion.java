package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.cp.dto.CpOpinionDTO;

@Entity
@Table(name = "cp_opinion")
@NoArgsConstructor
@Getter
public class CpOpinion extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpOpinionSeq;              // cp 의견 번호

    @Column
    private long userSeq;                   // 작성자

    @Column
    private long cpOpinionLocationSeq;      // cp 의견 위치 번호

    @Column
    private String cpOpinionContent;        // cp 의견 내용

    @Column
    private long cpOpinionViewCount;        // 조회수

    @Column
    private long keywordSeq;                // 키워드 번호

    @Builder
    public CpOpinion(
            long userSeq,
            long cpOpinionLocationSeq,
            String cpOpinionContent) {
    }

    public static CpOpinion toEntity(CpOpinionDTO cpOpinionDTO) {
        return CpOpinion.builder()
                .userSeq(cpOpinionDTO.getUserId())
                .cpOpinionLocationSeq(cpOpinionDTO.getCpOpinionLocationSeq())
                .cpOpinionContent(cpOpinionDTO.getCpOpinionContent())
                .build();
    }

    public void editCpOpinionContent(String cpOpinionContent) {
        this.cpOpinionContent = cpOpinionContent;
    }

    public void increaseCpOpinionViewCount() {
        this.cpOpinionViewCount++;
    }
}
