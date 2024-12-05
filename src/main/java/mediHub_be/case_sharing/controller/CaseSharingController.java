package mediHub_be.case_sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.case_sharing.dto.*;
import mediHub_be.case_sharing.service.CaseSharingService;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.security.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("case_sharing")
@RequiredArgsConstructor
@Tag(name = "케이스 공유", description = "의사들의 케이스 공유 API")
public class CaseSharingController {

    private final CaseSharingService caseSharingService;

    @Operation(summary = "케이스 공유 전체 목록 조회",
            description = "필터링 되지 않은 최신 버전 케이스 공유 전체 글 목록 조회")
    @GetMapping
    public ApiResponse<List<CaseSharingListDTO>> getAllCases() {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<CaseSharingListDTO> caseSharingList = caseSharingService.getCaseList(userSeq);
        return ApiResponse.ok(caseSharingList);
    }

    @Operation(summary = "케이스 공유 상세 조회",
            description = "케이스 공유 내용,사진,키워드,버전정보 포함한 전체 글 조회 ")
    @GetMapping("/{caseSharingSeq}")
    public ApiResponse<CaseSharingDetailDTO> getCaseDetail(@PathVariable("caseSharingSeq") Long caseSharingSeq) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        log.info("내정보"+userSeq);
        CaseSharingDetailDTO caseSharingDetailDTO = caseSharingService.getCaseSharingDetail(caseSharingSeq, userSeq);
        return ApiResponse.ok(caseSharingDetailDTO);
    }

    @Operation(summary = "케이스 공유 파트 별 조회",
            description = "파트에 따른 최신 버전 케이스 공유 글 목록 조회")
    @GetMapping("/part/{partSeq}")
    public ApiResponse<List<CaseSharingListDTO>> getCasesByPart(@PathVariable("partSeq") Long partSeq) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<CaseSharingListDTO> caseSharingList = caseSharingService.getCasesByPart(partSeq,userSeq);
        return ApiResponse.ok(caseSharingList);
    }

    @Operation(summary = "케이스 공유 버전 이력 조회",
            description = "같은 케이스 공유 그룹 글의 버전 이력 조회")
    @GetMapping("/versions/{caseSharingSeq}")
    public ApiResponse<List<CaseSharingVersionListDTO>> getCasesByGroup(@PathVariable("caseSharingSeq") Long caseSharingSeq) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        List<CaseSharingVersionListDTO> caseSharingVersionListDTOList = caseSharingService.getCaseVersionList(caseSharingSeq,userSeq);
        return ApiResponse.ok(caseSharingVersionListDTOList);

    }

    @Operation(summary = "케이스 공유글 등록",
            description = "케이스 공유 템플릿 선택 후 글 작성 및 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createCaseSharing(@RequestBody CaseSharingCreateRequestDTO requestDTO) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Long caseSharingSeq = caseSharingService.createCaseSharing(requestDTO,userSeq);
        return ApiResponse.created(caseSharingSeq);
    }

    @Operation(summary = "케이스 공유글 수정 (새 버전 생성)",
            description = "기존 케이스 공유글을 수정하고 새로운 버전을 생성합니다.")
    @PostMapping("/{caseSharingSeq}/versions")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createNewVersion(
            @PathVariable Long caseSharingSeq,
            @RequestBody CaseSharingUpdateRequestDTO requestDTO
    ) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Long newVersionSeq = caseSharingService.createNewVersion(caseSharingSeq, requestDTO,userSeq);
        return ApiResponse.created(newVersionSeq); // 새로 생성된 버전 ID 반환
    }

    @DeleteMapping("/{caseSharingSeq}")
    @Operation(summary = "케이스 공유글 삭제", description = "케이스 공유글을 소프트 삭제합니다.")
    public ApiResponse<Void> deleteCaseSharing(@PathVariable Long caseSharingSeq) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        caseSharingService.deleteCaseSharing(caseSharingSeq,userSeq);
        return ApiResponse.ok(null);
    }

    @Operation(summary = "임시 저장 등록", description = "케이스 공유 글을 임시 저장합니다.")
    @PostMapping("/draft")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> saveDraft(@RequestBody CaseSharingCreateRequestDTO requestDTO) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Long caseSharingSeq = caseSharingService.saveDraft(requestDTO, userSeq);
        return ApiResponse.created(caseSharingSeq);
    }




}
