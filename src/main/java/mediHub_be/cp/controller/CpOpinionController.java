package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.CpOpinionDTO;
import mediHub_be.cp.dto.RequestCpOpinionDTO;
import mediHub_be.cp.dto.ResponseCpOpinionDTO;
import mediHub_be.cp.dto.ResponseCpOpinionWithKeywordListAndCpOpinionVoteDTO;
import mediHub_be.cp.service.CpOpinionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        try {
            // CP 번호로 CP 의견을 가져오는 서비스 호출
            List<ResponseCpOpinionWithKeywordListAndCpOpinionVoteDTO> cpOpinionList = cpOpinionService.getCpOpinionListByCpVersionSeq(cpVersionSeq, cpOpinionLocationSeq, isDeleted);

            logger.info("CP 위치 번호로 CP 의견 리스트 조회 성공");
            logger.info("조회된 CP 의견 리스트의 크기: {}", cpOpinionList.size());

            return ResponseEntity.ok(ApiResponse.ok(cpOpinionList));
        } catch (DataAccessException e) {
            logger.error("CP 의견 위치 리스트를 가져오는 동안 CustomException이 발생했습니다: {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR);
        } catch (Exception e) {
            logger.error("CP 의견 위치 리스트를 가져오는 동안 예기치 않은 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
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

        try {
            // CP 의견을 가져오는 서비스 호출
            ResponseCpOpinionDTO cpOpinion = cpOpinionService.findCpOpinionByCpOpinionSeq(cpOpinionSeq);

            logger.info("CP 의견 번호로 CP 의견 조회 성공");
            return ResponseEntity.ok(ApiResponse.ok(cpOpinion));
        } catch (CustomException e) {
            logger.error("CP 의견 조회 중 CustomException 발생: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (Exception e) {
            logger.error("CP 의견 조회 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
    }

    // CP 의견 생성
    @PostMapping(value = "/{cpOpinionLocationSeq}")
    @Operation(summary = "CP 의견 생성",
            description = "주어진 CP 버전 시퀀스와 CP 의견 위치를 사용하여 CP 의견을 생성합니다.")
    public ResponseEntity<ApiResponse<CpOpinionDTO>> createCpOpinion(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpOpinionLocationSeq,
            @RequestBody RequestCpOpinionDTO requestBody) {

        logger.info("CP 의견 생성 요청: cpVersionSeq = {}, cpOpinionLocationSeq = {}, 요청 본문 = {}", cpVersionSeq, cpOpinionLocationSeq, requestBody);

        try {
            CpOpinionDTO cpOpinion = cpOpinionService.createCpOpinion(cpVersionSeq, cpOpinionLocationSeq, requestBody);

            logger.info("CP 의견이 성공적으로 생성되었습니다. CP 의견 정보: {}", cpOpinion);
            return ResponseEntity.ok(ApiResponse.created(cpOpinion));
        } catch (CustomException e) {
            logger.error("CP 의견 생성 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (Exception e) {
            logger.error("CP 의견 생성 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
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

        try {
            cpOpinionService.deleteByCpOpinionSeq(cpOpinionSeq);
            logger.info("CP 의견이 성공적으로 삭제되었습니다. cpOpinionSeq: {}", cpOpinionSeq);
        } catch (CustomException e) {
            logger.error("CP 의견 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (Exception e) {
            logger.error("CP 의견 삭제 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }

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

        try {
            // 요청 본문을 전달하여 CP 의견 업데이트
            CpOpinionDTO cpOpinionDTO = cpOpinionService.updateCpOpinionByCpOpinionSeq(cpOpinionSeq, requestBody);

            logger.info("CP 의견이 성공적으로 수정되었습니다: {}", cpOpinionDTO);
            return ResponseEntity.ok(ApiResponse.ok(cpOpinionDTO));
        } catch (CustomException e) {
            logger.error("CP 의견 수정 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                    .body(ApiResponse.fail(e));
        } catch (Exception e) {
            logger.error("CP 의견 수정 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
    }
}
