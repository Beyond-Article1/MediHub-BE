package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseSharingMyListDTO {

    private Long caseSharingSeq; // seq값
    private String caseSharingTitle; //제목
    private LocalDateTime regDate; //작성 일자
    private Long caseSharingViewCount; //조회수
}
