package mediHub_be.cp.dto;

import lombok.Data;

@Data
public class CpSearchDataDTO {

    private long cpSearchDataSeq;               // cp 검색 데이터 번호
    private long cpVersionSeq;                  // cp 버전 번호
    private long cpSearchCategoryDataSeq;       // cp 카테고리 데이터 번호
}
