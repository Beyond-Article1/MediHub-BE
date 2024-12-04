package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.service.CpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "cp")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CP", description = "CP 관련 API")
public class CpController {

    // Service
    private final CpService cpService;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.controller.CpController"); // Logger

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResponseCpDTO>>> getCpListByCpSearchCategoryAndCpSearchCategoryData(
            @RequestParam(required = false) String cpSearchCategorySeq,
            @RequestParam(required = false) String cpSearchCategoryData) {

        logger.info("카테고리 시퀀스: {} 및 카테고리 데이터: {}를 사용하여 CP 리스트 요청을 받았습니다.", cpSearchCategorySeq, cpSearchCategoryData);

        // RequestParam 값을 List에 저장(없으면, null)
        List<Long> cpSearchCategorySeqList = cpSearchCategorySeq != null
                ? Arrays.stream(cpSearchCategorySeq.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList()) : null;

        // RequestParam 값을 List에 저장(없으면, null)
        List<Long> cpSearchCategoryDataList =
                cpSearchCategoryData != null
                        ? Arrays.stream(cpSearchCategoryData.split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList()) : null;

        try {
            // 입력받은 데이터를 통하여 CP 리스트를 가져오는 서비스 호출
            List<ResponseCpDTO> cpList = cpService.getCpListByCpSearchCategoryAndCpSearchCategoryData(cpSearchCategorySeqList, cpSearchCategoryDataList);

            if (cpList == null || cpList.isEmpty()) {
                logger.info("주어진 카테고리 시퀀스 및 카테고리 데이터에 대한 CP 레코드가 없습니다.");
                return ResponseEntity.ok(ApiResponse.ok(cpList));
            }
            // 성공적인 응답 반환
            logger.info("조회된 CP 리스트 크기: {}", cpList.size());
            return ResponseEntity.ok(ApiResponse.ok(cpList));
        } catch (CustomException e) {
            logger.error("CP 리스트를 가져오는 동안 CustomException이 발생했습니다: {}", e.getMessage());
            // 예외 발생 시 실패 응답 반환
            return ResponseEntity
                    .status(e.getErrorCode().getHttpStatus())
                    .body(ApiResponse.fail(e));
        } catch (Exception e) {
            logger.error("CP 리스트를 가져오는 동안 예기치 않은 오류가 발생했습니다: {}", e.getMessage(), e);
            // 일반 예외 처리
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
    }

    // https://medihub.info/cp?cpName=value
    @GetMapping(params = "cpName") // 중복된 매핑 방지
    public ResponseEntity<ApiResponse<List<ResponseCpDTO>>> getCpByCpName(@RequestParam String cpName) {
        logger.info("이름: {}으로 CP를 가져오는 요청을 받았습니다.", cpName);
        try {
            // 이름을 통하여 Cp 를 가져오는 서비스 호출
            List<ResponseCpDTO> findCp = cpService.getCpListByCpName(cpName);

            if (findCp == null || findCp.isEmpty()) {
                logger.warn("이름 '{}'에 대한 CP 레코드가 없습니다.", cpName);
            } else {
                logger.info("이름으로 성공적으로 CP를 가져왔습니다: {}", findCp.size());
            }

            // 성공적인 응답 반환
            return ResponseEntity.ok(ApiResponse.ok(findCp));
        } catch (CustomException e) {
            logger.error("이름으로 CP를 가져오는 동안 CustomException이 발생했습니다: {}", e.getMessage());
            // 예외 발생 시 실패 응답 반환
            return ResponseEntity
                    .status(e.getErrorCode().getHttpStatus())
                    .body(ApiResponse.fail(e));
        } catch (Exception e) {
            logger.error("이름으로 CP를 가져오는 동안 예기치 않은 오류가 발생했습니다: {}", e.getMessage(), e);
            // 일반 예외 처리
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
    }

    // https://medihub.info/cp/{cpVersionSeq}
    @GetMapping(value = "/{cpVersionSeq}")
    public ResponseEntity<ApiResponse<ResponseCpDTO>> getCpByCpVersionSeq(@PathVariable long cpVersionSeq) {
        logger.info("버전 시퀀스: {}로 CP를 가져오는 요청을 받았습니다.", cpVersionSeq);
        try {
            // Cp 버전을 통하여 Cp 를 가져오는 서비스 호출
            ResponseCpDTO findCp = cpService.getCpByCpVersionSeq(cpVersionSeq);

            if (findCp == null) {
                logger.warn("버전 시퀀스 '{}'에 대한 CP 레코드가 없습니다.", cpVersionSeq);
                return ResponseEntity.ok(ApiResponse.ok(null)); // 빈 응답
            }

            logger.info("버전 시퀀스로 성공적으로 CP를 가져왔습니다: {}", findCp);
            // 성공적인 응답 반환
            return ResponseEntity.ok(ApiResponse.ok(findCp));
        } catch (CustomException e) {
            logger.error("버전 시퀀스로 CP를 가져오는 동안 CustomException이 발생했습니다: {}", e.getMessage());
            // 예외 발생 시 실패 응답 반환
            return ResponseEntity
                    .status(e.getErrorCode().getHttpStatus())
                    .body(ApiResponse.fail(e));
        } catch (Exception e) {
            logger.error("버전 시퀀스로 CP를 가져오는 동안 예기치 않은 오류가 발생했습니다: {}", e.getMessage(), e);
            // 일반 예외 처리
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
    }
}
