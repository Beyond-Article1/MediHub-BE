package mediHub_be.case_sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.case_sharing.dto.*;
import mediHub_be.case_sharing.service.CaseSharingService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/case_sharing")
@RequiredArgsConstructor
@Tag(name = "케이스 공유", description = "의사들의 케이스 공유 API")
public class CaseSharingController {

    private final CaseSharingService caseSharingService;

    @Operation(summary = "케이스 공유 전체 목록 조회", description = "필터링 되지 않은 최신 버전 케이스 공유 전체 글 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CaseSharingListDTO>>> getAllCases() {
        String userId = SecurityUtil.getCurrentUserId();
        List<CaseSharingListDTO> caseSharingList = caseSharingService.getCaseList(userId);
        return ResponseEntity.ok(ApiResponse.ok(caseSharingList));
    }

    @Operation(summary = "케이스 공유 상세 조회", description = "케이스 공유 내용, 사진, 키워드, 버전 정보 포함한 전체 글 조회")
    @GetMapping("/{caseSharingSeq}")
    public ResponseEntity<ApiResponse<CaseSharingDetailDTO>> getCaseDetail(@PathVariable("caseSharingSeq") Long caseSharingSeq,
                                                                           HttpServletRequest request,
                                                                           HttpServletResponse response) {
        String userId = SecurityUtil.getCurrentUserId();
        CaseSharingDetailDTO caseSharingDetailDTO = caseSharingService.getCaseSharingDetail(caseSharingSeq, userId,request,response);
        return ResponseEntity.ok(ApiResponse.ok(caseSharingDetailDTO));
    }

    @Operation(summary = "케이스 공유 메인 top3 조회", description = "메인 화면에 들어갈 일주일 내 작성된 게시글 중 조회수 top3 조회")
    @GetMapping("/top3")
    public ResponseEntity<ApiResponse<List<CaseSharingMain3DTO>>> getCaseTop3() {
        List<CaseSharingMain3DTO> top3Cases = caseSharingService.getTop3Cases();
        return ResponseEntity.ok(ApiResponse.ok(top3Cases));
    }



    @Operation(summary = "케이스 공유 파트 별 조회", description = "파트에 따른 최신 버전 케이스 공유 글 목록 조회")
    @GetMapping("/part/{partSeq}")
    public ResponseEntity<ApiResponse<List<CaseSharingListDTO>>> getCasesByPart(@PathVariable("partSeq") Long partSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        List<CaseSharingListDTO> caseSharingList = caseSharingService.getCasesByPart(partSeq, userId);
        return ResponseEntity.ok(ApiResponse.ok(caseSharingList));
    }

    @Operation(summary = "케이스 공유 버전 이력 조회", description = "같은 케이스 공유 그룹 글의 버전 이력 조회")
    @GetMapping("/versions/{caseSharingSeq}")
    public ResponseEntity<ApiResponse<List<CaseSharingVersionListDTO>>> getCasesByGroup(@PathVariable("caseSharingSeq") Long caseSharingSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        List<CaseSharingVersionListDTO> caseSharingVersionListDTOList = caseSharingService.getCaseVersionList(caseSharingSeq, userId);
        return ResponseEntity.ok(ApiResponse.ok(caseSharingVersionListDTOList));
    }

    @Operation(summary = "케이스 공유글 등록", description = "케이스 공유 템플릿 선택 후 글 작성 및 등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> createCaseSharing(
            @RequestPart("data") CaseSharingCreateRequestDTO requestDTO, // JSON 데이터
            @RequestPart(value = "images", required = false) List<MultipartFile> pictures // 이미지 파일
    ) {
        String userId = SecurityUtil.getCurrentUserId();
        Long caseSharingSeq = caseSharingService.createCaseSharing(requestDTO, pictures, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(caseSharingSeq));
    }
    
    @Operation(summary = "케이스 공유글 수정 (새 버전 생성)", description = "기존 케이스 공유글을 수정하고 새로운 버전을 생성합니다.")
    @PostMapping(value = "/{caseSharingSeq}/versions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> createNewVersion(
            @PathVariable Long caseSharingSeq,
            @RequestPart CaseSharingUpdateRequestDTO requestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> pictures // 이미지 파일
    ) {
        String userId = SecurityUtil.getCurrentUserId();
        Long newVersionSeq = caseSharingService.createNewVersion(caseSharingSeq, requestDTO, pictures, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(newVersionSeq));
    }

    @Operation(summary = "케이스 공유글 삭제", description = "케이스 공유글을 소프트 삭제합니다.")
    @DeleteMapping("/{caseSharingSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteCaseSharing(@PathVariable Long caseSharingSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        caseSharingService.deleteCaseSharing(caseSharingSeq, userId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "임시 저장 등록", description = "케이스 공유 글을 임시 저장합니다.")
    @PostMapping(value ="/draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<ApiResponse<Long>> saveDraft(
            @RequestPart CaseSharingCreateRequestDTO requestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> pictures // 이미지 파일
    ) {
        String userId = SecurityUtil.getCurrentUserId();
        Long caseSharingSeq = caseSharingService.saveDraft(requestDTO, pictures, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(caseSharingSeq));
    }

    @Operation(summary = "임시 저장 목록 조회", description = "임시 저장된 케이스 공유 글 목록을 조회합니다.")
    @GetMapping("/drafts")
    public ResponseEntity<ApiResponse<List<CaseSharingDraftListDTO>>> getDrafts() {
        String userId = SecurityUtil.getCurrentUserId();
        List<CaseSharingDraftListDTO> drafts = caseSharingService.getDraftsByUser(userId);
        return ResponseEntity.ok(ApiResponse.ok(drafts));
    }

    @Operation(summary = "임시 저장 내용 상세 조회", description = "임시 저장된 케이스 공유 글의 상세 정보를 반환합니다.")
    @GetMapping("/drafts/{caseSharingSeq}")
    public ResponseEntity<ApiResponse<CaseSharingDraftDetailDTO>> getDraftsDetail(@PathVariable("caseSharingSeq") Long caseSharingSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        CaseSharingDraftDetailDTO draftDetailDTO = caseSharingService.getDraftDetail(caseSharingSeq, userId);
        return ResponseEntity.ok(ApiResponse.ok(draftDetailDTO));
    }

    @Operation(summary = "임시 저장 내용 수정", description = "임시 저장 글의 제목과 내용, keyword 등을 수정합니다.")
    @PutMapping(value= "/drafts/{caseSharingSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> updateDraft(
            @PathVariable("caseSharingSeq") Long caseSharingSeq,
            @RequestPart CaseSharingDraftUpdateDTO updateDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> pictures // 이미지 파일

    ) {
        String userId = SecurityUtil.getCurrentUserId();
        Long updatedSeq = caseSharingService.updateDraft(caseSharingSeq, userId, pictures, updateDTO);
        return ResponseEntity.ok(ApiResponse.ok(updatedSeq));
    }

    @Operation(summary = "임시 저장 삭제", description = "임시 저장된 케이스 공유를 삭제합니다.")
    @DeleteMapping("/drafts/{caseSharingSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteDraft(@PathVariable("caseSharingSeq") Long caseSharingSeq) {
        String userId = SecurityUtil.getCurrentUserId();
        caseSharingService.deleteDraft(caseSharingSeq, userId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("/{caseSharingSeq}/bookmark")
    @Operation(summary = "케이스 공유글 북마크", description = "케이스 공유글 북마크를 등록/해제 합니다.")
    public ResponseEntity<ApiResponse<Boolean>> toggleBookmark(
            @PathVariable Long caseSharingSeq
    ) {
        String userId = SecurityUtil.getCurrentUserId();
        boolean isBookmarked = caseSharingService.toggleBookmark(caseSharingSeq, userId);
        return ResponseEntity.ok(ApiResponse.ok(isBookmarked));
    }

    @Operation(summary = "북마크 여부 확인", description = "케이스 공유글에 대해 북마크 여부를 확인합니다.")
    @GetMapping("/{caseSharingSeq}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> isBookmarked(
            @PathVariable Long caseSharingSeq
    ) {
        String userId = SecurityUtil.getCurrentUserId();
        boolean isBookmarked = caseSharingService.isBookmarked(caseSharingSeq, userId);
        return ResponseEntity.ok(ApiResponse.ok(isBookmarked));
    }

    @Operation(summary = "내가 작성한 케이스 공유 목록 조회", description = "내가 작성한, 삭제되지 않은 최신 버전 케이스 공유 글 목록 조회")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<CaseSharingMyListDTO>>> getMyCases() {
        String userId = SecurityUtil.getCurrentUserId();
        List<CaseSharingMyListDTO> caseSharingMyList = caseSharingService.getMyCaseList(userId);
        return ResponseEntity.ok(ApiResponse.ok(caseSharingMyList));
    }

    @Operation(summary = "내가 북마크한 케이스 공유 목록 조회", description = "내가 북마크한, 삭제되지 않은 최신 버전 케이스 공유 글 목록 조회")
    @GetMapping("/my/bookmark")
    public ResponseEntity<ApiResponse<List<CaseSharingListDTO>>> getBookMarkedCases() {
        String userId = SecurityUtil.getCurrentUserId();
        List<CaseSharingListDTO> caseSharingList = caseSharingService.getBookMarkedCaseList(userId);
        return ResponseEntity.ok(ApiResponse.ok(caseSharingList));
    }



}
