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
public class CaseSharingCreateRequestDTO {
    private Long userSeq; // 작성자 seq
    private Long templateSeq; //템플릿 seq
    private String title; // 케이스 공유 제목
    private String content; // 케이스 공유 본문 내용
    private List<String> keywords; // 키워드 리스트
}
