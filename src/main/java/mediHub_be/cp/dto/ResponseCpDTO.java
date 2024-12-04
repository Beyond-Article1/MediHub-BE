package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ResponseCpDTO {

    private String cpName;                      // cp 명
    private String cpDescription;               // cp 설명
    private long cpViewCount;                   // cp 조회수
    private String cpVersion;                   // cp 버전
    private String cpVersionDescription;        // cp 버전 설명
    private LocalDateTime createdAt;            // cp 버전 생성일
    private String userName;                    // 작성자명
    private String userId;                      // 작성자 아이디
    private String partName;                    // 작성자 부서명

    // 변환 메소드
    public static ResponseCpDTO buildResponseCpDTO(Map<String, Object> map) {
        return ResponseCpDTO.builder()
                .cpName((String) map.get("cpName"))
                .cpDescription((String) map.get("cpDescription"))
                .cpViewCount((Long) map.get("cpViewCount"))
                .cpVersion((String) map.get("cpVersion"))
                .cpVersionDescription((String) map.get("cpVersionDescription"))
                .createdAt((LocalDateTime) map.get("createdAt"))
                .userName((String) map.get("userName"))
                .userId((String) map.get("userId"))
                .partName((String) map.get("partName"))
                .build();
    }
}
