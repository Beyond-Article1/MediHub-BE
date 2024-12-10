package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.ResponseCpSearchDataDTO;
import mediHub_be.cp.service.CpSearchDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{cpVersionSeq}")
    @Operation(summary = "CP 검색 데이터 조회",
            description = "주어진 CP 버전 시퀀스에 대한 CP 검색 데이터를 반환합니다.")
    public ResponseEntity<ApiResponse<List<ResponseCpSearchDataDTO>>> getCpSearchData(@PathVariable long cpVersionSeq) {
        logger.info("CP 검색 데이터 조회 요청: cpVersionSeq={}", cpVersionSeq); // 요청 시작 로그 추가

        List<ResponseCpSearchDataDTO> dtoList = cpSearchDataService.getCpSearchDataListByCpVersionSeq(cpVersionSeq);

        if (dtoList.isEmpty()) {
            // 비어있는 리스트에 대한 처리 (선택 사항)
            logger.warn("CP 검색 데이터가 없습니다: cpVersionSeq={}", cpVersionSeq);
        }

        return ResponseEntity.ok(ApiResponse.ok(dtoList));
    }
}
