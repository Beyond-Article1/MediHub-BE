package mediHub_be.cp.dto;

import lombok.Data;

@Data
public class CpDTO {

    private long cpSeq;                 // cp 번호
    private long userSeq;               // 등록자
    private String cpName;              // cp 명
    private String cpDescription;       // cp 설명
    private long cpViewCount;           // 조회수
}
