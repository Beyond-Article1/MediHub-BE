package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseCpSearchCategoryDataDTO {

    private long cpSearchCategoryDataSeq;           // cp 검색카테고리 데이터 번호
    private long cpSearchCategorySeq;               // cp 검색카테고리 번호
    private String cpSearchCategoryName;            // cp 검색카테고리 번호
    private String cpSearchCategoryDataName;        // cp 검색카테고리 데이터명
    private long userSeq;                           // 등록자
    private String userName;                        // 등록자명
    private String userId;                          // 등록자 ID
    private LocalDateTime createdAt;                // 생성일
    private LocalDateTime updatedAt;                // 수정일
    private LocalDateTime deletedAt;                // 삭제일
}
