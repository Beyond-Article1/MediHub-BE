package mediHub_be.medicalLife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import mediHub_be.common.response.ApiResponse;
import mediHub_be.medicalLife.dto.MedicalLifeCommentListDTO;
import mediHub_be.medicalLife.dto.MedicalLifeCommentRequestDTO;
import mediHub_be.medicalLife.dto.MedicalLifeCreateRequestDTO;
import mediHub_be.medicalLife.dto.MedicalLifeListDTO;
import mediHub_be.medicalLife.service.MedicalLifeService;
import mediHub_be.security.util.SecurityUtil;
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

    // 메디컬 라이프 전체 조회
    @Operation(summary = "메디컬 라이프 전체 조회", description = "필터링 되지 않은 메디컬 라이프 전체 글 목록 조회" )
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicalLifeListDTO>>> getMedicalLifeList() {

        // 회원 가져오기
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<MedicalLifeListDTO> medicalLifeListDTOList = medicalLifeService.getMedicalLifeList(userSeq);

        return ResponseEntity.ok(ApiResponse.ok(medicalLifeListDTOList));
    }

    // 메디컬 라이프 상세 조회
    @Operation(summary = "메디컬 라이프 게시물 전체 조회", description = "로그인한 사용자가 메디컬 라이프 게시물을 전체 조회합니다.")
    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<List<MedicalLifeListDTO>>> getMedicalLifeDetailList() {

         Long userSeq = SecurityUtil.getCurrentUserSeq();
         List<MedicalLifeListDTO> medicalLifeListDTOList = medicalLifeService.getMedicalLifeDetailList(userSeq);

         return ResponseEntity.ok(ApiResponse.ok(medicalLifeListDTOList));
    }

    // 메디컬 라이프 댓글 조회
    @Operation(summary = "메디컬 라이프 댓글 목록 조회", description = "메디컬 라이프 게시글에 달린 댓글 목록을 조회합니다.")
    @GetMapping("/{medicalLifeSeq}/comments")
    public ResponseEntity<ApiResponse<List<MedicalLifeCommentListDTO>>> getMedicalLifeCommentList(
            @PathVariable Long medicalLifeSeq) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<MedicalLifeCommentListDTO> commentList = medicalLifeService.getMedicalLifeCommentList(medicalLifeSeq, userSeq);

        return ResponseEntity.ok(ApiResponse.ok(commentList));
    }

    // 메디컬 라이프 게시글 작성
    @Operation(summary = "메디컬 라이프 댓글 등록", description = "메디컬 라이프 게시글을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createMedicalLife(
            @RequestPart("medicalLife") MedicalLifeCreateRequestDTO medicalLifeCreateRequestDTO,
            @RequestPart(value = "pictures", required = false) List<MultipartFile> pictureList) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();

        Long medicalLifeSeq = medicalLifeService.createMedicalLife(medicalLifeCreateRequestDTO, pictureList, userSeq);

        return ResponseEntity.ok(ApiResponse.ok(medicalLifeSeq));
    }

    // 댓글 작성
    @PostMapping("/{medicalLifeSeq}/comments")
    public ResponseEntity<ApiResponse<Long>> createMedicalLifeComment(
            @PathVariable Long medicalLifeSeq,
            @RequestBody MedicalLifeCommentRequestDTO medicalLifeCommentRequestDTO) {

        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Long commentSeq = medicalLifeService.createMedicalLifeComment(medicalLifeSeq, medicalLifeCommentRequestDTO, userSeq);

        return ResponseEntity.ok(ApiResponse.ok(commentSeq));
    }


}

