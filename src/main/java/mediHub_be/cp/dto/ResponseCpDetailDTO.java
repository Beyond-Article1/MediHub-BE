package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseCpDetailDTO {

    private long cpVersionSeq;                  // cp 버전 번호
    private String cpName;                      // cp 명
    private String cpDescription;               // cp 설명
    private long cpViewCount;                   // cp 조회수
    private String cpVersion;                   // cp 버전
    private String cpVersionDescription;        // cp 버전 설명
    private LocalDateTime createdAt;            // cp 버전 생성일
    private String userName;                    // 작성자명
    private String userId;                      // 작성자 아이디
    private String partName;                    // 작성자 과명(ex: 외과/내과/안과 ...)
    private boolean isBookmarked;               // 북마크여부
    private String pictureUrl;                  // 프로필 사진

    // 변환 메소드
    public static ResponseCpDetailDTO toDto(ResponseCpDTO dto, String pictureUrl) {
        return ResponseCpDetailDTO.builder()
                .cpVersionSeq(dto.getCpVersionSeq())
                .cpName(dto.getCpName())
                .cpDescription(dto.getCpDescription())
                .cpViewCount(dto.getCpViewCount())
                .cpVersionDescription(dto.getCpVersionDescription())
                .createdAt(dto.getCreatedAt())
                .userName(dto.getUserName())
                .userId(dto.getUserId())
                .isBookmarked(dto.isBookmarked())
                .pictureUrl(pictureUrl)
                .build();
    }
}
