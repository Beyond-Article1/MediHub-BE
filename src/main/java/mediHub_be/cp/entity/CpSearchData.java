package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.cp.dto.RequestCpSearchDataDTO;

@Entity
@Table(name = "cp_search_data")
@Getter
@NoArgsConstructor
public class CpSearchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpSearchDataSeq; // cp 검색 데이터 번호

    @Column(nullable = false) // NOT NULL 제약 조건 추가
    private long cpVersionSeq; // cp 버전 번호

    @Column(nullable = false) // NOT NULL 제약 조건 추가
    private long cpSearchCategoryDataSeq; // cp 카테고리 데이터 번호

    @Builder
    public CpSearchData(long cpVersionSeq,
                        long cpSearchCategoryDataSeq) {
        this.cpVersionSeq = cpVersionSeq;
        this.cpSearchCategoryDataSeq = cpSearchCategoryDataSeq;
    }

    public static CpSearchData create(RequestCpSearchDataDTO requestBody) {
        return CpSearchData.builder()
                .cpVersionSeq(requestBody.getCpVersionSeq())
                .cpSearchCategoryDataSeq(requestBody.getCpSearchCategoryDataSeq())
                .build();
    }
}
