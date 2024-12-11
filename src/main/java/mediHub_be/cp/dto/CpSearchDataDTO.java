package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;
import mediHub_be.cp.entity.CpSearchData;

@Data
@Builder
public class CpSearchDataDTO {

    private long cpSearchDataSeq;               // cp 검색 데이터 번호
    private long cpVersionSeq;                  // cp 버전 번호
    private long cpSearchCategoryDataSeq;       // cp 카테고리 데이터 번호

    public static CpSearchDataDTO toDto(CpSearchData entity) {
        return CpSearchDataDTO.builder()
                .cpSearchDataSeq(entity.getCpSearchDataSeq())
                .cpVersionSeq(entity.getCpVersionSeq())
                .cpSearchCategoryDataSeq(entity.getCpSearchCategoryDataSeq())
                .build();
    }
}
