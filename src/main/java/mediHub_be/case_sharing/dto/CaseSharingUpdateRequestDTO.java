package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseSharingUpdateRequestDTO {

    private Long userSeq; // 작성자 ID
    private String title; // 새로운 제목
    private String content; // 새로운 본문 내용
    private List<String> keywords; // 새로운 키워드 리스트
}
