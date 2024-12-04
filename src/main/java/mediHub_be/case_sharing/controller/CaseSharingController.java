package mediHub_be.case_sharing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.case_sharing.dto.CaseSharingListDTO;
import mediHub_be.case_sharing.dto.CaseSharingDetailDTO;
import mediHub_be.case_sharing.service.CaseSharingService;
import mediHub_be.common.response.ApiResponse;
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
            description = "필터링 되지 않은 케이스 공유 전체 글 목록 조회")
    @GetMapping
    public ApiResponse<List<CaseSharingListDTO>> getAllCases() {
        List<CaseSharingListDTO> caseSharingList = caseSharingService.getCaseList();
        return ApiResponse.ok(caseSharingList);
    }

    @Operation(summary = "케이스 공유 상세 조회",
            description = "케이스 공유 내용,사진, 키워드 포함한 전체 글 조회 ")
    @GetMapping("/{caseSharingSeq}")
    public ApiResponse<CaseSharingDetailDTO> getCaseDetail(@PathVariable("caseSharingSeq") Long caseSharingSeq) {
        CaseSharingDetailDTO caseSharingDetailDTO = caseSharingService.getCaseSharingDetail(caseSharingSeq);
        return ApiResponse.ok(caseSharingDetailDTO);
    }


    /*@Operation(summary = "케이스 공유글 등록",
            description = "케이스 공유 템플릿 선택 후 글 작성 및 등록")
    @GetMapping("/")*/
}