package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.ResponseCpSearchCategoryDataDTO;
import mediHub_be.cp.service.CpSearchCategoryDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "cp/cpSearchCategory/{cpSearchCategorySeq}/cpSearchCategoryData")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CP", description = "CP 관련 API")
public class CpSearchCategoryDataController {

    // Service
    private final CpSearchCategoryDataService cpSearchCategoryDataService;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.controller.CpController"); // Logger

    // CP 검색 카테고리 데이터 전체 조회
    @GetMapping
    @Operation(summary = "CP 검색 카테고리 데이터 조회",
            description = "주어진 CP 검색 카테고리 ID에 대한 모든 데이터를 조회하여 리스트로 반환합니다.")
    public ResponseEntity<ApiResponse<List<ResponseCpSearchCategoryDataDTO>>> getCpSearchCategoryDataList(@PathVariable long cpSearchCategorySeq) {

        List<ResponseCpSearchCategoryDataDTO> dtoList;

        logger.info("CP 검색 카테고리 데이터 조회 요청: ID={}", cpSearchCategorySeq);

        dtoList = cpSearchCategoryDataService.getCpSearchCategoryDataListByCpSearchCategorySeq(cpSearchCategorySeq);
        logger.info("CP 검색 카테고리 데이터 조회 성공: ID={} 데이터 수={}", cpSearchCategorySeq, dtoList.size());

        return ResponseEntity.ok(ApiResponse.ok(dtoList));
    }

    // CP 검색 카테고리 데이터 단일 조회
    @GetMapping(value = "/{cpSearchCategoryDataSeq}")
    @Operation(summary = "CP 검색 카테고리 데이터 조회",
            description = "주어진 CP 검색 카테고리 시퀀스와 CP 검색 카테고리 데이터 시퀀스를 사용하여 데이터를 조회합니다.")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDataDTO>> getCpSearchCategoryData(
            @PathVariable long cpSearchCategorySeq,
            @PathVariable long cpSearchCategoryDataSeq) {

        logger.info("CP 검색 카테고리 번호: {}, CP 검색 카테고리 데이터 번호: {}로 조회 요청했습니다.", cpSearchCategorySeq, cpSearchCategoryDataSeq);

        ResponseCpSearchCategoryDataDTO cpSearchCategoryData = cpSearchCategoryDataService.getCpSearchCategoryData(cpSearchCategorySeq, cpSearchCategoryDataSeq);
        logger.info("CP 검색 카테고리 데이터 조회 성공: {}", cpSearchCategoryData);

        return ResponseEntity.ok(ApiResponse.ok(cpSearchCategoryData));
    }

    @PostMapping
    @Operation(summary = "CP 검색 카테고리 데이터 생성",
            description = "주어진 CP 검색 카테고리 시퀀스를 사용하여 새로운 CP 검색 카테고리 데이터를 생성합니다.")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDataDTO>> createCpSearchCategoryData(
            @PathVariable long cpSearchCategorySeq,
            @RequestBody String cpSearchCategoryDataName) {

        logger.info("CP 검색 카테고리 데이터 생성 요청: cpSearchCategorySeq={}, cpSearchCategoryDataName={}", cpSearchCategorySeq, cpSearchCategoryDataName);

        ResponseCpSearchCategoryDataDTO dto = cpSearchCategoryDataService.createCpSearchCategoryData(cpSearchCategorySeq, cpSearchCategoryDataName);
        logger.info("CP 검색 카테고리 데이터 성공적으로 생성됨: {}", dto);

        // 성공적으로 생성된 경우 201 Created 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(dto));
    }

    // CP 검색 카테고리 데이터 수정
    @PutMapping(value = "/{cpSearchCategoryDataSeq}")
    @Operation(summary = "CP 검색 카테고리 데이터 수정",
            description = "주어진 CP 검색 카테고리 시퀀스와 데이터 시퀀스를 사용하여 해당 카테고리 데이터를 수정합니다.")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDataDTO>> updateCpSearchCategoryData(
            @PathVariable long cpSearchCategorySeq,
            @PathVariable long cpSearchCategoryDataSeq,
            @RequestBody String cpSearchCategoryDataName) {

        logger.info("CP 검색 카테고리 데이터 업데이트 요청: cpSearchCategorySeq={}, cpSearchCategoryDataSeq={}, 새 이름={}",
                cpSearchCategorySeq, cpSearchCategoryDataSeq, cpSearchCategoryDataName);

        ResponseCpSearchCategoryDataDTO dto = cpSearchCategoryDataService.updateCpSearchCategoryDataData(cpSearchCategoryDataSeq, cpSearchCategoryDataName);
        logger.info("CP 검색 카테고리 데이터 업데이트 성공: ID={}", cpSearchCategoryDataSeq);

        // 성공적으로 수정된 경우 200 OK 응답 반환
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    // CP 검색 카테고리 데이터 삭제
    @DeleteMapping(value = "/{cpSearchCategoryDataSeq}")
    @Operation(summary = "CP 검색 카테고리 데이터 삭제",
            description = "주어진 CP 검색 카테고리 ID와 데이터 ID로 CP 검색 카테고리 데이터를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteCpSearchCategoryData(
            @PathVariable long cpSearchCategorySeq,
            @PathVariable long cpSearchCategoryDataSeq) {

        logger.info("CP 검색 카테고리 데이터 삭제 요청: cpSearchCategorySeq={}, cpSearchCategoryDataSeq={}",
                cpSearchCategorySeq, cpSearchCategoryDataSeq);

        cpSearchCategoryDataService.deleteCpSearchCategoryData(cpSearchCategoryDataSeq);
        logger.info("CP 검색 카테고리 데이터 삭제 성공: ID={}", cpSearchCategoryDataSeq);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
