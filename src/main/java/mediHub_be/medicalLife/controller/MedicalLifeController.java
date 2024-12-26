package mediHub_be.medicalLife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.medicalLife.dto.*;
import mediHub_be.medicalLife.service.MedicalLifeService;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "medical-life")
@RequiredArgsConstructor
@Tag(name = "메디컬 라이프", description = "메디컬 라이프 API")
public class MedicalLifeController {

    private final MedicalLifeService medicalLifeService;

    @Operation(summary = "메디컬 라이프 전체 조회", description = "필터링 되지 않은 메디컬 라이프 전체 글 목록 조회" )
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicalLifeListDTO>>> getMedicalLifeList() {

        // 회원 가져오기
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<MedicalLifeListDTO> medicalLifeListDTOList = medicalLifeService.getMedicalLifeList(userSeq);

        return ResponseEntity.ok(ApiResponse.ok(medicalLifeListDTOList));
    }

    @Operation(summary = "메디컬 라이프 상세 조회", description = "특정 메디컬 라이프 게시물의 상세 정보를 조회합니다.")
    @GetMapping("/detail/{medicalLifeSeq}")
    public ResponseEntity<ApiResponse<MedicalLifeDetailDTO>> getMedicalLifeDetail(
            @PathVariable("medicalLifeSeq") Long medicalLifeSeq,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        MedicalLifeDetailDTO medicalLifeDetailDTO = medicalLifeService.getMedicalLifeDetail(medicalLifeSeq, userSeq, request, response);

        return ResponseEntity.ok(ApiResponse.ok(medicalLifeDetailDTO));
    }

    @Operation(summary = "메디컬 라이프 댓글 목록 조회", description = "메디컬 라이프 게시글에 달린 댓글 목록을 조회합니다.")
    @GetMapping("/{medicalLifeSeq}/comments")
    public ResponseEntity<ApiResponse<List<MedicalLifeCommentListDTO>>> getMedicalLifeCommentList(
            @PathVariable Long medicalLifeSeq) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<MedicalLifeCommentListDTO> commentList = medicalLifeService.getMedicalLifeCommentList(medicalLifeSeq, userSeq);

        return ResponseEntity.ok(ApiResponse.ok(commentList));
    }

    @Operation(summary = "메디컬 라이프 게시글 등록", description = "메디컬 라이프 게시글을 작성 및 등록합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> createMedicalLife(
            @RequestPart("data") MedicalLifeCreateRequestDTO medicalLifeCreateRequestDTO,
            @RequestPart(value = "pictures", required = false) List<MultipartFile> pictureList
    ) {
        Long userSeq = SecurityUtil.getCurrentUserSeq(); // 현재 사용자 시퀀스 가져오기
        Long medicalLifeSeq = medicalLifeService.createMedicalLife(
                medicalLifeCreateRequestDTO,
                pictureList,
                userSeq
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(medicalLifeSeq));
    }

    @Operation(summary = "댓글 작성", description = "메디컬 라이프 댓글을 작성합니다.")
    @PostMapping("/{medicalLifeSeq}/comments")
    public ResponseEntity<ApiResponse<Long>> createMedicalLifeComment(
            @PathVariable("medicalLifeSeq") Long medicalLifeSeq,
            @RequestBody MedicalLifeCommentRequestDTO medicalLifeCommentRequestDTO) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Long commentSeq = medicalLifeService.createMedicalLifeComment(medicalLifeSeq, medicalLifeCommentRequestDTO, userSeq);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(commentSeq));
    }

    @Operation(summary = "메디컬 라이프 게시글 수정", description = "메디컬 라이프 게시글의 제목, 내용, 키워드 등을 수정")
    @PutMapping(value = "/{medicalLifeSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> updateMedicalLife(
            @PathVariable("medicalLifeSeq") Long medicalLifeSeq,
            @RequestPart MedicalLifeUpdateRequestDTO medicalLifeUpdateRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> newImageList
    ) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Long updatedMedicalLifeSeq = medicalLifeService.updateMedicalLife(
                medicalLifeSeq,
                medicalLifeUpdateRequestDTO,
                newImageList,
                userSeq
        );

        return ResponseEntity.ok(ApiResponse.ok(updatedMedicalLifeSeq));
    }

    @Operation(summary = "메디컬 라이프 댓글 수정", description = "메디컬 라이프 댓글의 내용을 수정")
    @PutMapping(value = "/{medicalLifeSeq}/comment/{commentSeq}")
    public ResponseEntity<ApiResponse<Long>> updateMedicalLifeComment(
            @PathVariable("medicalLifeSeq") Long medicalLifeSeq,
            @PathVariable("commentSeq") Long commentSeq,
            @RequestBody MedicalLifeCommentRequestDTO medicalLifeCommentRequestDTO
    ) {

        log.info("Received request to update comment");
        log.info("medicalLifeSeq: {}, commentSeq: {}, commentContent: {}",
                medicalLifeSeq, commentSeq, medicalLifeCommentRequestDTO.getCommentContent());

        Long userSeq = SecurityUtil.getCurrentUserSeq();

        Long updatedCommentSeq = medicalLifeService.updateMedicalLifeComment(
                medicalLifeSeq,
                commentSeq,
                medicalLifeCommentRequestDTO,
                userSeq
        );

        return ResponseEntity.ok(ApiResponse.ok(updatedCommentSeq));
    }

    @Operation(summary = "메디컬 라이프 게시글 삭제", description = "메디컬 라이프 게시글을 소프트 삭제합니다.")
    @DeleteMapping(value = "/{medicalLifeSeq}")
    public ResponseEntity<ApiResponse<String>> deleteMedicalLife(@PathVariable Long medicalLifeSeq) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        boolean isDeleted = medicalLifeService.deleteMedicalLife(medicalLifeSeq, userSeq);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(
                    new CustomException(ErrorCode.NOT_FOUND_MEDICAL_LIFE)
            ));
        }
    }

    @Operation(summary = "메디컬 라이프 댓글 삭제", description = "메디컬 라이프 댓글을 소프트 삭제합니다.")
    @DeleteMapping(value = "/{medicalLifeSeq}/comment/{commentSeq}")
    public ResponseEntity<ApiResponse<String>> deleteMedicalLifeComment(
            @PathVariable Long medicalLifeSeq,
            @PathVariable Long commentSeq
    ) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        boolean isDeleted = medicalLifeService.deleteMedicalLifeComment(medicalLifeSeq, commentSeq, userSeq);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail(
                    new CustomException(ErrorCode.UNAUTHORIZED_USER)
            ));
        }
    }

    @Operation(summary = "메디컬 라이프 북마크", description = "메디컬 라이프 북마크를 등록/해제")
    @PatchMapping("/{medicalLifeSeq}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> toggleBookmark(@PathVariable Long medicalLifeSeq) {
        String userId = SecurityUtil.getCurrentUserId();

        boolean isBookmarked = medicalLifeService.toggleBookmark(medicalLifeSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isBookmarked));
    }

    @Operation(summary = "북마크 여부 확인", description = "메디컬 라이프에 대해 북마크 여부 확인")
    @GetMapping("/{medicalLifeSeq}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> isBookmarked(@PathVariable Long medicalLifeSeq) {
        String userId = SecurityUtil.getCurrentUserId();

        boolean isBookmarked = medicalLifeService.isBookmarked(medicalLifeSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isBookmarked));
    }

    @Operation(summary = "메디컬 라이프 좋아요", description = "메디컬 라이프 좋아요를 등록/해제")
    @PatchMapping("/{medicalLifeSeq}/prefer")
    public ResponseEntity<ApiResponse<Boolean>> togglePrefer(@PathVariable Long medicalLifeSeq) {
        String userId = SecurityUtil.getCurrentUserId();

        boolean isPreferred = medicalLifeService.togglePrefer(medicalLifeSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isPreferred));
    }

    @Operation(summary = "좋아요 여부 확인", description = "메디컬 라이프에 대해 좋아요 여부 확인")
    @GetMapping("/{medicalLifeSeq}/prefer")
    public ResponseEntity<ApiResponse<Boolean>> isPreferred(@PathVariable Long medicalLifeSeq) {
        String userId = SecurityUtil.getCurrentUserId();

        boolean isPreferred = medicalLifeService.isPreferred(medicalLifeSeq, userId);

        return ResponseEntity.ok(ApiResponse.ok(isPreferred));
    }

    @Operation(summary = "내가 작성한 메디컬 라이프 게시글 목록 조회", description = "내가 작성한, 삭제되지 않은 메디컬 라이프 게시글 목록 조회")
    @GetMapping("/mypage")
    public ResponseEntity<ApiResponse<List<MedicalLifeMyListDTO>>> getMyMedicalLifePosts() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();

        List<MedicalLifeMyListDTO> medicalLifeMyDTOList = medicalLifeService.getMyMedicalLifeList(userSeq);

        return ResponseEntity.ok(ApiResponse.ok(medicalLifeMyDTOList));
    }

    @Operation(summary = "내가 북마크 한 메디컬 라이프 목록 조회", description = "내가 북마크 한, 삭제되지 않은 메디컬 라이프 목록 조회")
    @GetMapping("/mypage/bookmark")
    public ResponseEntity<ApiResponse<List<MedicalLifeListDTO>>> getBookMarkedMedicalLifeList() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();

        List<MedicalLifeListDTO> medicalLifeListDTOList = medicalLifeService.getBookMarkedMedicalLifeList(userSeq);

        return ResponseEntity.ok(ApiResponse.ok(medicalLifeListDTOList));
    }

    @Operation(summary = "메디컬 라이프 Top 3 조회", description = "조회수가 가장 높은 메디컬 라이프 게시글 Top 3를 반환합니다.")
    @GetMapping("/top3")
    public ResponseEntity<ApiResponse<List<MedicalLifeTop3DTO>>> getTop3MedicalLifeByViewCount() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();

        List<MedicalLifeTop3DTO> top3MedicalLifeList = medicalLifeService.getTop3MedicalLifeByViewCount(userSeq);

        return ResponseEntity.ok(ApiResponse.ok(top3MedicalLifeList));
    }

}

