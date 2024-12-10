package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseCpSearchCategoryDTO {

    private long cpSearchCategorySeq;           // cp 검색카테고리 번호
    private String cpSearchCategoryName;        // cp 검색카테고리명
    private LocalDateTime createdAt;            // 생성일
    private LocalDateTime updatedAt;            // 수정일
    private LocalDateTime deletedAt;            // 삭제일
    private long userSeq;                       // 등록자 번호
    private String userName;                    // 등록자 이름
    private String userId;                      // 등록자 아이디
}
