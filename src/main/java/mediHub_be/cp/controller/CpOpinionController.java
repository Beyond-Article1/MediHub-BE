package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.CpOpinionDTO;
import mediHub_be.cp.dto.RequestCpOpinionDTO;
import mediHub_be.cp.dto.ResponseCpOpinionDTO;
import mediHub_be.cp.dto.ResponseCpOpinionWithKeywordListAndCpOpinionVoteDTO;
import mediHub_be.cp.service.CpOpinionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "cp/{cpVersionSeq}/cpOpinionLocation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CP", description = "CP 관련 API")
public class CpOpinionController {

    // Service
    private final CpOpinionService cpOpinionService;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.controller.CpOpinionController"); // Logger

    // 특정 위치의 CP 의견 리스트 전체 조회
    @GetMapping(value = "/{cpOpinionLocationSeq}")
    @Operation(summary = "CP 의견 리스트 조회",
            description = "주어진 CP 버전 시퀀스와 CP 의견 위치 시퀀스를 기준으로 CP 의견 리스트를 조회합니다.")
    public ResponseEntity<ApiResponse<List<ResponseCpOpinionWithKeywordListAndCpOpinionVoteDTO>>> getCpOpinionListByCpOpinionLocationSeq(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpOpinionLocationSeq,
            @RequestParam(required = false, defaultValue = "false") boolean isDeleted) {

        logger.info("CP 버전 시퀀스: {}의 CP 의견 위치 시퀀스: {}에 위치의 CP 의견 리스트 요청을 받았습니다.", cpVersionSeq, cpOpinionLocationSeq);

        List<ResponseCpOpinionWithKeywordListAndCpOpinionVoteDTO> cpOpinionList = cpOpinionService.getCpOpinionListByCpVersionSeq(cpVersionSeq, cpOpinionLocationSeq, isDeleted);

        logger.info("CP 위치 번호로 CP 의견 리스트 조회 성공");
        logger.info("조회된 CP 의견 리스트의 크기: {}", cpOpinionList.size());
        return ResponseEntity.ok(ApiResponse.ok(cpOpinionList));

    }

    // CP 의견 상세 조회
    @GetMapping("/{cpOpinionLocationSeq}/{cpOpinionSeq}")
    @Operation(summary = "CP 의견 조회",
            description = "주어진 CP 버전 시퀀스와 CP 의견 위치, CP 의견 번호를 사용하여 CP 의견을 조회합니다.")
    public ResponseEntity<ApiResponse<ResponseCpOpinionDTO>> getCpOpinionByCpOpinionSeq(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpOpinionLocationSeq,
            @PathVariable long cpOpinionSeq) {

        logger.info("CP 버전 번호: {}, CP 의견 위치 번호: {}, CP 의견 번호: {}로 조회 요청했습니다.", cpVersionSeq, cpOpinionLocationSeq, cpOpinionSeq);

        ResponseCpOpinionDTO cpOpinion = cpOpinionService.findCpOpinionByCpOpinionSeq(cpOpinionSeq);

        logger.info("CP 의견 번호로 CP 의견 조회 성공");
        return ResponseEntity.ok(ApiResponse.ok(cpOpinion));

    }

    // CP 의견 생성
    @PostMapping(value = "/{cpOpinionLocationSeq}")
    @Operation(summary = "CP 의견 생성",
            description = "주어진 CP 버전 시퀀스와 CP 의견 위치를 사용하여 CP 의견을 생성합니다.")
    public ResponseEntity<ApiResponse<CpOpinionDTO>> createCpOpinion(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpOpinionLocationSeq,
            @RequestBody RequestCpOpinionDTO requestBody,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageList) {

        logger.info("CP 의견 생성 요청: cpVersionSeq = {}, cpOpinionLocationSeq = {}, 요청 본문 = {}", cpVersionSeq, cpOpinionLocationSeq, requestBody);

        CpOpinionDTO cpOpinion = cpOpinionService.createCpOpinion(cpVersionSeq, cpOpinionLocationSeq, requestBody, imageList);

        logger.info("CP 의견이 성공적으로 생성되었습니다. CP 의견 정보: {}", cpOpinion);
        return ResponseEntity.ok(ApiResponse.created(cpOpinion));
    }

    // CP 의견 삭제
    @DeleteMapping(value = "/{cpOpinionLocationSeq}/{cpOpinionSeq}")
    @Operation(summary = "CP 의견 삭제",
            description = "주어진 CP 버전 시퀀스와 CP 의견 위치, CP 의견 번호를 사용하여 CP 의견을 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> deleteCpOpinionByCpOpinionSeq(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpOpinionLocationSeq,
            @PathVariable long cpOpinionSeq) {

        logger.info("CP 의견 삭제 요청. CP 의견 ID: {}", cpOpinionSeq);

        cpOpinionService.deleteByCpOpinionSeq(cpOpinionSeq);
        logger.info("CP 의견이 성공적으로 삭제되었습니다. cpOpinionSeq: {}", cpOpinionSeq);

        return ResponseEntity.ok(ApiResponse.ok("CP 의견이 성공적으로 삭제되었습니다."));
    }

    // CP 의견 수정
    @PutMapping(value = "/{cpOpinionLocationSeq}/{cpOpinionSeq}")
    @Operation(summary = "CP 의견 수정",
            description = "주어진 CP 버전 시퀀스와 CP 의견 위치, CP 의견 번호를 사용하여 CP 의견을 수정합니다.")
    public ResponseEntity<ApiResponse<CpOpinionDTO>> updateCpOpinion(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpOpinionLocationSeq,
            @PathVariable long cpOpinionSeq,
            @RequestBody RequestCpOpinionDTO requestBody) {

        logger.info("CP 의견 수정 요청: cpOpinionSeq = {}, requestBody = {}", cpOpinionSeq, requestBody);

        CpOpinionDTO cpOpinionDTO = cpOpinionService.updateCpOpinionByCpOpinionSeq(cpOpinionSeq, requestBody);

        logger.info("CP 의견이 성공적으로 수정되었습니다: {}", cpOpinionDTO);
        return ResponseEntity.ok(ApiResponse.ok(cpOpinionDTO));
    }
}
