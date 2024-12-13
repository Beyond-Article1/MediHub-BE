package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseSimpleCpSearchCategoryDataDTO {

    private long cpSearchCategoryDataSeq;           // cp 검색카테고리 데이터 번호
    private String cpSearchCategoryDataName;        // cp 검색카테고리 데이터명
}
