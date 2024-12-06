package mediHub_be.case_sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.case_sharing.dto.*;
import mediHub_be.case_sharing.service.TemplateService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("template")
@RequiredArgsConstructor
@Tag(name = "케이스 공유 템플릿", description = "케이스 공유 템플릿 관련 API")
public class TemplateController {

    private final TemplateService templateService;

    @Operation(summary = "회원 템플릿 전체 조회", description = "회원이 볼 수 있는 모든 템플릿 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TemplateListDTO>>> getAllTemplates() {
        String userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.ok(templateService.getAllTemplates(userId)));
    }

    @Operation(summary = "조건별 템플릿 조회", description = "내 템플릿, 과 공유 템플릿, 전체 공개 템플릿 조회")
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<TemplateListDTO>>> getTemplatesByFilter(@RequestParam("filter") String filter) {
        String userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.ok(templateService.getTemplatesByFilter(userId, filter)));
    }

    @Operation(summary = "템플릿 상세 조회", description = "특정 템플릿의 상세 내용을 조회합니다.")
    @GetMapping("/{templateSeq}")
    public ResponseEntity<ApiResponse<TemplateDetailDTO>> getTemplateDetail(@PathVariable("templateSeq") Long templateSeq) {
        TemplateDetailDTO templateDetail = templateService.getTemplateDetail(templateSeq);
        return ResponseEntity.ok(ApiResponse.ok(templateDetail));
    }

    @Operation(summary = "템플릿 등록", description = "새로운 케이스 공유 템플릿 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createTemplate(@RequestBody TemplateRequestDTO requestDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.ok(templateService.createTemplate(userId, requestDTO)));
    }

    @Operation(summary = "템플릿 수정", description = "기존 템플릿을 수정합니다.")
    @PutMapping("/{templateSeq}")
    public ResponseEntity<ApiResponse<Void>> updateTemplate(@PathVariable("templateSeq") Long templateSeq,
                                                            @RequestBody TemplateRequestDTO requestDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        templateService.updateTemplate(userId, templateSeq, requestDTO );
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "템플릿 삭제", description = "기존 템플릿 삭제")
    @DeleteMapping("/{templateSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable Long templateSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        templateService.deleteTemplate(userId, templateSeq);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
