package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCpSearchData {
    private long cpVersionSeq;              // CP 버전
    private long cpSearchCategoryDataSeq;   // CP 검색 카테고리
}
