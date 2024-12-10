package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.CpSearchDataDTO;
import mediHub_be.cp.dto.RequestCpSearchDataDTO;
import mediHub_be.cp.dto.ResponseCpSearchDataDTO;
import mediHub_be.cp.service.CpSearchDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "cp/{cpVersionSeq}/cpSearchData")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CP", description = "CP 관련 API")
public class CpSearchDataController {

    // Service
    private final CpSearchDataService cpSearchDataService;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.controller.CpSearchDataController"); // Logger

    @GetMapping(value = "/{cpVersionSeq}")
    @Operation(summary = "CP 검색 데이터 조회",
            description = "주어진 CP 버전 시퀀스에 대한 CP 검색 데이터를 반환합니다.")
    public ResponseEntity<ApiResponse<List<ResponseCpSearchDataDTO>>> getCpSearchData(@PathVariable long cpVersionSeq) {
        logger.info("CP 검색 데이터 조회 요청: cpVersionSeq={}", cpVersionSeq);

        List<ResponseCpSearchDataDTO> dtoList = cpSearchDataService.getCpSearchDataListByCpVersionSeq(cpVersionSeq);

        if (dtoList.isEmpty()) {
            // 비어있는 리스트에 대한 처리 (선택 사항)
            logger.warn("CP 검색 데이터가 없습니다: cpVersionSeq={}", cpVersionSeq);
        }

        return ResponseEntity.ok(ApiResponse.ok(dtoList));
    }

    @PostMapping
    @Operation(summary = "CP 검색 데이터 생성",
            description = "주어진 정보를 기반으로 새로운 CP 검색 데이터를 생성합니다.")
    public ResponseEntity<ApiResponse<CpSearchDataDTO>> createCpSearchData(@RequestBody RequestCpSearchDataDTO requestBody) {
        logger.info("CP 검색 데이터 생성 요청: {}", requestBody);

        CpSearchDataDTO dto = cpSearchDataService.createCpSearchData(requestBody);
        logger.info("CP 검색 데이터 생성 성공: {}", dto);

        return ResponseEntity.ok(ApiResponse.created(dto));
    }

    @DeleteMapping(value = "/{cpVersionSeq}/{cpSearchDataSeq}")
    @Operation(
            summary = "CP 검색 데이터 삭제",
            description = "주어진 CP 버전 시퀀스와 CP 검색 데이터 시퀀스를 사용하여 해당 CP 검색 데이터를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteCpSearchData(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpSearchDataSeq) {
        logger.info("{}번 CP 버전 전호에 {}번 CP 검색 데이터 번호에 대한 삭제를 요청했습니다.", cpVersionSeq, cpSearchDataSeq);

        cpSearchDataService.deleteCpSearchData(cpSearchDataSeq);
        logger.info("{}번 CP 버전 전호에 {}번 CP 검색 데이터 번호에 대한 삭제 하였습니다.", cpVersionSeq, cpSearchDataSeq);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
