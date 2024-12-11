package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCpSearchDataDTO {

    private long cpSearchDataSeq;               // cp 검색 데이터 번호
    private long cpVersionSeq;                  // cp 버전 번호
    private String cpName;                      // cp 이름
    private long cpSearchCategoryDataSeq;       // cp 카테고리 데이터 번호
    private String cpSearchCategoryName;        // cp 카테고리 데이터명
}
