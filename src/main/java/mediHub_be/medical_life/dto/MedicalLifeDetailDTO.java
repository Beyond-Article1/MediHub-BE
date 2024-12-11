package mediHub_be.medical_life.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MedicalLifeDetailDTO {
    private Long medicalLifeSeq;          // 게시글 ID
    private String userName;              // 작성자 이름
    private String deptName;              // 부서 이름
    private String partName;              // 과 이름
    private String medicalLifeTitle;      // 게시글 제목
    private String medicalLifeContent;    // 게시글 내용
    private Long medicalLifeViewCount;    // 조회수
    private LocalDateTime createdAt;      // 작성일
    private List<MedicalLifePictureDTO> pictures; // 사진 리스트
    private List<MedicalLifeCommentDTO> comments;
    private List<MedicalLifeKeywordDTO> keywords; // 키워드 리스트
}
