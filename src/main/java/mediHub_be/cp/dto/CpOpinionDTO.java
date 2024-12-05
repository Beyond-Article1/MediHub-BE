package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;
import mediHub_be.cp.entity.CpOpinion;
import mediHub_be.security.util.SecurityUtil;

import java.time.LocalDateTime;

@Data
@Builder
public class CpOpinionDTO {

    private Long cpOpinionSeq;              // cp 의견 번호
    private Long userId;                    // 작성자
    private Long cpOpinionLocationSeq;      // cp 의견 위치 번호
    private String cpOpinionContent;        // cp 의견 내용
    private Long cpOpinionViewCount;        // 조회수
    private Long keywordSeq;                // 키워드 번호
    private LocalDateTime createdAt;        // 생성일
    private LocalDateTime updatedAt;        // 수정일
    private LocalDateTime deletedAt;        // 삭제일

    public static CpOpinionDTO create(
            long cpOpinionLocationSeq,
            RequestCpOpinionDTO requestBody) {
        return CpOpinionDTO.builder()
                .cpOpinionSeq(null)
                .userId(SecurityUtil.getCurrentUserSeq())
                .cpOpinionLocationSeq(cpOpinionLocationSeq)
                .cpOpinionContent(requestBody.getCpOpinionContent())
                .cpOpinionViewCount(0L)
                .keywordSeq(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .build();
    }

    public static CpOpinionDTO toDto(CpOpinion cpOpinion) {
        return CpOpinionDTO.builder()
                .cpOpinionSeq(cpOpinion.getCpOpinionSeq())
                .userId(cpOpinion.getUserSeq())
                .cpOpinionLocationSeq(cpOpinion.getCpOpinionLocationSeq())
                .cpOpinionContent(cpOpinion.getCpOpinionContent())
                .cpOpinionViewCount(cpOpinion.getCpOpinionViewCount())
                .keywordSeq(cpOpinion.getKeywordSeq())
                .createdAt(cpOpinion.getCreatedAt())
                .updatedAt(cpOpinion.getUpdatedAt())
                .deletedAt(cpOpinion.getDeletedAt())
                .build();
    }
}
