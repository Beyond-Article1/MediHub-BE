package mediHub_be.cp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CpVersionDTO {

    private long cpVersionSeq;              // cp 버전번호
    private long cpSeq;                     // cp 번호
    private long userSeq;                   // cp 등록자
    private String cpVersion;               // cp 버전
    private String cpVersionDescription;    // cp 버전 설명
    private String cpUrl;                   // cp url
    private LocalDateTime createAt;         // 생성일
}
