package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseCpOpinionDTO {

    private String userName;                // 작성자명
    private String userId;                  // 장성자 아이디
    private String partName;                // 작성자 과명(ex: 외과/내과/안과 ...
    private String cpOpinionContent;        // cp 의견 내용
    private LocalDateTime createdAt;        // 생성일
    private LocalDateTime updatedAt;        // 수정일
}
