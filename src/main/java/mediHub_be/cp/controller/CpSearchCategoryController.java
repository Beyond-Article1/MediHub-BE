package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.ResponseCpSearchCategoryAndCpSearchCategoryDataDTO;
import mediHub_be.cp.dto.ResponseCpSearchCategoryDTO;
import mediHub_be.cp.service.CpSearchCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "cp/cpSearchCategory")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CP", description = "CP 관련 API")
public class CpSearchCategoryController {

    // Service
    private final CpSearchCategoryService cpSearchCategoryService;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.controller.cpSearchCategoryController"); // Logger

    // CP 검색 카테고리 전체 조회
    @GetMapping
    @Operation(summary = "CP 검색 카테고리 전체 조회",
            description = "모든 CP 검색 카테고리를 조회하여 리스트로 반환합니다. ")
    public ResponseEntity<ApiResponse<List<ResponseCpSearchCategoryDTO>>> getCpSearchCategoryList() {

        logger.info("CP 검색 카테고리 조회 요청");

        List<ResponseCpSearchCategoryDTO> dtoList = cpSearchCategoryService.getCpSearchCategoryList();
        logger.info("CP 검색 카테고리 조회 성공! 크기: {}", dtoList.size());
//        logger.info("조회된 결과: {}", dtoList);

        return ResponseEntity.ok(ApiResponse.ok(dtoList));
    }

    // CP 검색 카테고리 상세 조회
    @GetMapping(value = "/{cpSearchCategorySeq}")
    @Operation(summary = "CP 검색 카테고리 상세 조회",
            description = "지정된 CP 검색 카테고리의 상세 정보를 반환합니다. " +
                    "주어진 카테고리 번호에 해당하는 정보를 조회하여, " +
                    "해당 카테고리가 존재하지 않을 경우 적절한 오류 메시지를 반환합니다.")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDTO>> getCpSearchCategory(@PathVariable long cpSearchCategorySeq) {

        ResponseCpSearchCategoryDTO dto;

        logger.info("CP 검색 카테고리 조회 요청: cpSearchCategorySeq={}", cpSearchCategorySeq);

        dto = cpSearchCategoryService.getCpSearchCategoryByCpSearchCategorySeq(cpSearchCategorySeq);
        logger.info("CP 검색 카테고리 조회 성공: {}", dto);

        logger.info("CP 검색 카테고리 조회 결과 반환: {}", dto);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    // CP 검색 카테고리 생성
    @PostMapping
    @Operation(summary = "CP 검색 카테고리 생성", description = "제공된 이름으로 새로운 CP 검색 카테고리를 생성합니다.")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDTO>> createCpSearchCategory(@RequestBody String cpSearchCategoryName) {

        // 요청받은 카테고리 이름 로그
        logger.info("CP 검색 카테고리 생성 요청: {}", cpSearchCategoryName);

        // 서비스 메서드를 호출하여 카테고리 생성
        ResponseCpSearchCategoryDTO dto = cpSearchCategoryService.createCpSearchCategory(cpSearchCategoryName);
        logger.info("CP 검색 카테고리 성공적으로 생성됨: {}", dto);

        // 성공적으로 생성된 경우 201 Created 응답 반환
        return ResponseEntity.ok(ApiResponse.created(dto));
    }

    // CP 검색 카테고리 수정
    @PutMapping(value = "{cpSearchCategorySeq}")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDTO>> updateCpSearchCategory(
            @PathVariable long cpSearchCategorySeq,
            @RequestBody String cpSearchCategoryName) {

        logger.info("CP 검색 카테고리 수정 요청: ID={}, 새 이름={}", cpSearchCategorySeq, cpSearchCategoryName);

        ResponseCpSearchCategoryDTO dto = cpSearchCategoryService.updateCpSearchCategory(cpSearchCategorySeq, cpSearchCategoryName);
        logger.info("CP 검색 카테고리 수정 성공: ID={}", cpSearchCategorySeq);

        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    // CP 검색 카테고리 삭제
    @DeleteMapping(value = "/{cpSearchCategorySeq}")
    @Operation(summary = "CP 검색 카테고리 삭제", description = "주어진 ID로 CP 검색 카테고리를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteCpSearchCategory(@PathVariable long cpSearchCategorySeq) {
        logger.info("CP 검색 카테고리 삭제 요청: ID={}", cpSearchCategorySeq);

        cpSearchCategoryService.deleteCpSearchCategory(cpSearchCategorySeq);
        logger.info("CP 검색 카테고리 삭제 성공: ID={}", cpSearchCategorySeq);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping(value = "/cpSearchCategoryData")
    @Operation(summary = "CP 검색 카테고리 데이터 조회",
            description = "CP 검색 카테고리와 각 카테고리에 대한 데이터를 조회하여 반환합니다.")
    public ResponseEntity<ApiResponse<List<ResponseCpSearchCategoryAndCpSearchCategoryDataDTO>>> getCpSearchCategoryAndCpSearchCategoryData() {
        logger.info("CP 검색 카테고리 별 데이터 조회 요청이 수신되었습니다.");

        List<ResponseCpSearchCategoryAndCpSearchCategoryDataDTO> dtoList = cpSearchCategoryService.getCpSearchCategoryAndCpSearchCategoryData();

        if (dtoList == null || dtoList.isEmpty()) {
            logger.warn("조회된 CP 검색 카테고리 데이터가 없습니다.");
            return ResponseEntity.ok(ApiResponse.ok(Collections.emptyList()));
        }

        logger.info("CP 검색 카테고리 별 데이터 조회가 완료되었습니다. 조회된 데이터 수: {}", dtoList.size());
        return ResponseEntity.ok(ApiResponse.ok(dtoList));
    }
}