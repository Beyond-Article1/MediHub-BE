package mediHub_be.medical_life.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.medical_life.dto.DeptPartFilterDTO;
import mediHub_be.medical_life.dto.MedicalLifeCommentDTO;
import mediHub_be.medical_life.dto.MedicalLifeDTO;
import mediHub_be.medical_life.dto.MedicalLifeDetailDTO;
import mediHub_be.medical_life.service.MedicalLifeService;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "메디컬 라이프", description = "메디컬 라이프 API")
@RestController
@RequestMapping(value = "/medicalLife")
@RequiredArgsConstructor
public class MedicalLifeController {

    private final MedicalLifeService medicalLifeService;

    @Operation(summary = "메디컬 라이프 게시글 목록 조회", description = "필터를 사용해 게시글 목록 반환")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicalLifeDTO>>> getAllMedicalLifePosts(
            @RequestParam(required = false) Long deptSeq,
            @RequestParam(required = false) Long partSeq
    ) {
        String userId = SecurityUtil.getCurrentUserId();

        DeptPartFilterDTO filterDTO = new DeptPartFilterDTO();
        List<MedicalLifeDTO> medicalLifeDTOList = medicalLifeService.getMedicalLifeListByUsername(userId, filterDTO);
        filterDTO.setDeptSeq(deptSeq);
        filterDTO.setPartSeq(partSeq);

        return ResponseEntity.ok(ApiResponse.ok(medicalLifeDTOList));
    }

    @Operation(summary = "메디컬 라이프 게시글 상세 조회", description = "특정 게시글의 상세 정보를 반환")
    @GetMapping("/{medicalLifeSeq}")
    public ResponseEntity<ApiResponse<MedicalLifeDetailDTO>> getMedicalLifeDetail(
            @PathVariable Long medicalLifeSeq,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String userId = SecurityUtil.getCurrentUserId();
        MedicalLifeDetailDTO medicalLifeDetailDTO = medicalLifeService.getMedicalLifeDetail(medicalLifeSeq, userId, request, response);

        return ResponseEntity.ok(ApiResponse.ok(medicalLifeDetailDTO));
    }

    @Operation(summary = "메디컬 라이프 게시글 등록", description = "새로운 게시글 등록")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Long>> createMedicalLifePost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        String userId = SecurityUtil.getCurrentUserId();
        Long medicalLifeSeq = medicalLifeService.createMedicalLife(title, content, images, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(medicalLifeSeq));
    }

    @Operation(summary = "메디컬 라이프 게시글 수정", description = "기존 게시글 수정")
    @PutMapping(value = "/{medicalLifeSeq}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<String>> updateMedicalLifePost(
            @PathVariable Long medicalLifeSeq,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        String userId = SecurityUtil.getCurrentUserId();
        medicalLifeService.updateMedicalLife(medicalLifeSeq, title, content, images, userId);

        return ResponseEntity.ok(ApiResponse.ok("게시글이 성공적으로 수정되었습니다."));
    }

    @Operation(summary = "메디컬 라이프 게시글 삭제", description = "게시글 삭제")
    @DeleteMapping("/{medicalLifeSeq}")
    public ResponseEntity<ApiResponse<String>> deleteMedicalLifePost(@PathVariable Long medicalLifeSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        medicalLifeService.deleteMedicalLife(medicalLifeSeq, userId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 댓글 목록")
    @GetMapping("/{medicalLifeSeq}/comments")
    public ResponseEntity<ApiResponse<List<MedicalLifeCommentDTO>>> getComments(@PathVariable Long medicalLifeSeq) {
        List<MedicalLifeCommentDTO> comments = medicalLifeService.getMedicalLifeComments(medicalLifeSeq);

        return ResponseEntity.ok(ApiResponse.ok(comments));
    }

    @Operation(summary = "댓글 등록", description = "특정 게시글에 댓글 등록")
    @PostMapping("/{medicalLifeSeq}/comments")
    public ResponseEntity<ApiResponse<Long>> createComment(
            @PathVariable Long medicalLifeSeq,
            @RequestParam("content") String content
    ) {
        String userId = SecurityUtil.getCurrentUserId();
        Long commentSeq = medicalLifeService.createMedicalLifeComment(medicalLifeSeq, content, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(commentSeq));
    }

    @Operation(summary = "댓글 수정", description = "기존 댓글 수정")
    @PutMapping("/{medicalLifeSeq}/comments/{commentSeq}")
    public ResponseEntity<ApiResponse<String>> updateComment(
            @PathVariable Long medicalLifeSeq,
            @PathVariable Long commentSeq,
            @RequestParam("content") String content
    ) {
        String userId = SecurityUtil.getCurrentUserId();
        medicalLifeService.updateMedicalLifeComment(commentSeq, content, userId);

        return ResponseEntity.ok(ApiResponse.ok("댓글이 성공적으로 수정되었습니다."));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/{medicalLifeSeq}/comments/{commentSeq}")
    public ResponseEntity<ApiResponse<String>> deleteComment(
            @PathVariable Long medicalLifeSeq,
            @PathVariable Long commentSeq
    ) {
        String userId = SecurityUtil.getCurrentUserId();
        medicalLifeService.deleteMedicalLifeComment(commentSeq, userId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "북마크 설정/해제", description = "게시글 북마크 등록/해제")
    @PatchMapping("/{medicalLifeSeq}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> toggleBookmark(@PathVariable Long medicalLifeSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        boolean isBookmarked = medicalLifeService.toggleBookmark(medicalLifeSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isBookmarked));
    }

    @Operation(summary = "좋아요 설정/해제", description = "게시글 좋아요 등록/해제")
    @PatchMapping("/{medicalLifeSeq}/prefer")
    public ResponseEntity<ApiResponse<Boolean>> togglePrefer(@PathVariable Long medicalLifeSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        boolean isPreferred = medicalLifeService.togglePrefer(medicalLifeSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isPreferred));
    }

    @Operation(summary = "북마크 여부 확인", description = "특정 게시글에 대해 북마크 여부.")
    @GetMapping("/{medicalLifeSeq}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> isBookmarked(@PathVariable Long medicalLifeSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        boolean isBookmarked = medicalLifeService.isBookmarked(medicalLifeSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isBookmarked));
    }

    @Operation(summary = "좋아요 여부 확인", description = "특정 게시글에 대해 좋아요 여부.")
    @GetMapping("/{medicalLifeSeq}/prefer")
    public ResponseEntity<ApiResponse<Boolean>> isPreferred(@PathVariable Long medicalLifeSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        boolean isPreferred = medicalLifeService.isPreferred(medicalLifeSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isPreferred));
    }

    @Operation(summary = "내가 작성한 게시글 조회", description = "현재 사용자가 작성한 게시글 목록")
    @GetMapping("/mymeidicallife")
    public ResponseEntity<ApiResponse<List<MedicalLifeDTO>>> getMyPosts() {
        String userId = SecurityUtil.getCurrentUserId();
        List<MedicalLifeDTO> myPosts = medicalLifeService.getMyMedicalLifePosts(userId);

        return ResponseEntity.ok(ApiResponse.ok(myPosts));
    }

    @Operation(summary = "내가 북마크한 게시글 조회", description = "현재 사용자가 북마크한 게시글 목록")
    @GetMapping("/mybookmarks")
    public ResponseEntity<ApiResponse<List<MedicalLifeDTO>>> getMyBookmarkedPosts() {
        String userId = SecurityUtil.getCurrentUserId();
        List<MedicalLifeDTO> bookmarkedPosts = medicalLifeService.getMyBookmarkedMedicalLifePosts(userId);

        return ResponseEntity.ok(ApiResponse.ok(bookmarkedPosts));
    }
}
