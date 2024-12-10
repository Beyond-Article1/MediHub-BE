package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import mediHub_be.cp.dto.RequestCpSearchData;

@Entity
@Table(name = "cp_search_data")
@Getter
public class CpSearchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpSearchDataSeq;               // cp 검색 데이터 번호

    @Column
    private long cpVersionSeq;                  // cp 버전 번호

    @Column
    private long cpSearchCategoryDataSeq;       // cp 카테고리 데이터 번호

    @Builder
    public CpSearchData(
            long cpVersionSeq,
            long cpSearchCategoryDataSeq) {
    }

    public static CpSearchData toEntity(RequestCpSearchData requestBody) {
        return CpSearchData.builder()
                .cpVersionSeq(requestBody.getCpVersionSeq())
                .cpSearchCategoryDataSeq(requestBody.getCpSearchCategoryDataSeq())
                .build();
    }
}
