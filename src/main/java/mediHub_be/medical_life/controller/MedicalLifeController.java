package mediHub_be.medical_life.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import mediHub_be.common.response.ApiResponse;
import mediHub_be.medical_life.dto.MedicalLifeCommentListDTO;
import mediHub_be.medical_life.dto.MedicalLifeListDTO;
import mediHub_be.medical_life.entity.MedicalLife;
import mediHub_be.medical_life.service.MedicalLifeService;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

