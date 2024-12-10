package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.CpOpinionLocationDTO;
import mediHub_be.cp.dto.RequestCpOpinionLocationDTO;
import mediHub_be.cp.service.CpOpinionLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "cp/{cpVersionSeq}/cpOpinionLocation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CP", description = "CP 관련 API")
public class CpOpinionLocationController {

    // Service
    private final CpOpinionLocationService cpOpinionLocationService;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.controller.CpController"); // Logger

    // CP 의견 위치 조회
    @GetMapping
    @Operation(summary = "CP 의견 위치 조회",
            description = "주어진 CP 버전 시퀀스를 사용하여 CP 의견 위치 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CpOpinionLocationDTO>>> getCpOpinionLocation(@PathVariable long cpVersionSeq) {

        logger.info("CP 버전 시퀀스: {}로 CP 의견 위치 조회 요청을 받았습니다.", cpVersionSeq);

        List<CpOpinionLocationDTO> cpOpinionLocationDtoList = cpOpinionLocationService.findCpOpinionLocationListByCpVersionSeq(cpVersionSeq);

        logger.info("CP 의견 위치 목록 조회 성공: {}", cpOpinionLocationDtoList);
        return ResponseEntity.ok(ApiResponse.ok(cpOpinionLocationDtoList));
    }

    // CP 의견 위치 생성
    @PostMapping
    @Operation(summary = "CP 의견 위치 생성",
            description = "주어진 CP 버전 시퀀스를 사용하여 새로운 CP 의견 위치를 생성합니다.")
    public ResponseEntity<ApiResponse<CpOpinionLocationDTO>> createCpOpinionLocation(
            @PathVariable long cpVersionSeq,
            @RequestBody RequestCpOpinionLocationDTO requestBody) {

        logger.info("CP 버전 시퀀스: {}로 새로운 CP 의견 위치 생성 요청을 받았습니다. requestBody: {}", cpVersionSeq, requestBody);

        CpOpinionLocationDTO dto = cpOpinionLocationService.createCpOpinionLocation(cpVersionSeq, requestBody);

        logger.info("CP 의견 위치가 성공적으로 생성되었습니다. 생성된 정보: {}", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(dto)); // 201 Created
    }

    // CP 의견 위치 삭제
    @DeleteMapping(value = "/{cpOpinionLocationSeq}")
    @Operation(summary = "CP 의견 위치 삭제",
            description = "지정된 CP 버전과 CP 의견 위치 번호에 해당하는 CP 의견 위치를 삭제합니다. ")
    public ResponseEntity<ApiResponse<Void>> deleteCpOpinionLocation(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpOpinionLocationSeq) {

        logger.info("CP 의견 위치 삭제 요청: cpVersionSeq={}, cpOpinionLocationSeq={}", cpVersionSeq, cpOpinionLocationSeq);

        cpOpinionLocationService.deleteCpOpinionLocation(cpVersionSeq, cpOpinionLocationSeq);
        logger.info("CP 의견 위치 삭제 성공: cpVersionSeq={}, cpOpinionLocationSeq={}", cpVersionSeq, cpOpinionLocationSeq);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
