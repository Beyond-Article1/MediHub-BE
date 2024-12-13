package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ResponseCpSearchCategoryAndCpSearchCategoryDataDTO {

    private long cpSearchCategorySeq;           // cp 검색카테고리 번호
    private String cpSearchCategoryName;        // cp 검색카테고리명
    private List<ResponseSimpleCpSearchCategoryDataDTO> cpSearchCategoryDataDtoList;    // 카테고리 별 데이터

    @Builder
    public ResponseCpSearchCategoryAndCpSearchCategoryDataDTO(
            long cpSearchCategorySeq,
            String cpSearchCategoryName) {
    }
}
