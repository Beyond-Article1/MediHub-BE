package mediHub_be.case_sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.case_sharing.dto.CaseSharingCommentDetailDTO;
import mediHub_be.case_sharing.dto.CaseSharingCommentListDTO;
import mediHub_be.case_sharing.dto.CaseSharingCommentRequestDTO;
import mediHub_be.case_sharing.dto.CaseSharingCreateRequestDTO;
import mediHub_be.case_sharing.service.CaseSharingCommentService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/case_sharing_comment")
@RequiredArgsConstructor
@Tag(name = "케이스 공유 댓글", description = "케이스 공유 댓글 관련 API")
public class CaseSharingCommentController {
    private final CaseSharingCommentService caseSharingCommentService;

    @Operation(summary = "케이스 공유글 댓글 목록 위치 반환", description = "(이모티콘 표시 위한) 해당 케이스 공유 글의 삭제되지 않은 댓글 위치 목록 반환")
    @GetMapping("/{caseSharingSeq}")
    public ResponseEntity<ApiResponse<List<CaseSharingCommentListDTO>>> getCaseSharingComments(@PathVariable("caseSharingSeq") Long caseSharingSeq){
        String userId = SecurityUtil.getCurrentUserId();
        List<CaseSharingCommentListDTO> caseSharingCommentList = caseSharingCommentService.getCommentList(userId, caseSharingSeq);
        return ResponseEntity.ok(ApiResponse.ok(caseSharingCommentList));
    }

    @Operation(summary = "케이스 공유글 특정 블록 댓글 조회", description = "특정 케이스 공유글 및 블록에 속하는 댓글 목록 조회")
    @GetMapping("/{caseSharingSeq}/comments/{blockId}")
    public ResponseEntity<ApiResponse<List<CaseSharingCommentDetailDTO>>> getCaseSharingBlockComments(
            @PathVariable("caseSharingSeq") Long caseSharingSeq,
            @PathVariable("blockId") String blockId) {
        String userId = SecurityUtil.getCurrentUserId();
        List<CaseSharingCommentDetailDTO> comments = caseSharingCommentService.getCommentsByBlock(userId, caseSharingSeq, blockId);
        return ResponseEntity.ok(ApiResponse.ok(comments));
    }


    @Operation(summary = "케이스 공유글 댓글 등록", description = "케이스 공유글에 대한 댓글 등록")
    @PostMapping("/{caseSharingSeq}")
    public ResponseEntity<ApiResponse<Long>> createCaseSharingComment(
            @PathVariable("caseSharingSeq") Long caseSharingSeq,
            @RequestBody CaseSharingCommentRequestDTO requestDTO){
        String userId = SecurityUtil.getCurrentUserId();
        Long commentSeq = caseSharingCommentService.createCaseSharingComment(userId, caseSharingSeq, requestDTO);
        return ResponseEntity.ok(ApiResponse.created(commentSeq));
    }
    @Operation(summary = "케이스 공유글 댓글 수정", description = "케이스 공유글 댓글 수정")
    @PutMapping("/{commentSeq}")
    public ResponseEntity<ApiResponse<Void>> updateCaseSharingComment(
            @PathVariable("commentSeq") Long commentSeq,
            @RequestBody CaseSharingCommentRequestDTO requestDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        caseSharingCommentService.updateCaseSharingComment(userId, commentSeq, requestDTO);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "케이스 공유글 댓글 삭제", description = "케이스 공유글 댓글 삭제")
    @DeleteMapping("/{commentSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteCaseSharingComment(@PathVariable("commentSeq") Long commentSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        caseSharingCommentService.deleteCaseSharingComment(userId, commentSeq);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

}
