package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.dto.ResponseCpDetailDTO;
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

    // CP 검색 조회
    // example: https://medihub.info/cp?cpSearchCategorySeq=1,2,3&cpSearchCategoryData=1,2,3
    @GetMapping
    @Operation(summary = "CP 리스트 조회",
            description = "주어진 카테고리 시퀀스와 카테고리 데이터를 사용하여 CP 리스트를 조회하거나, 주어진 CP 이름으로 조회합니다.")
    public ResponseEntity<ApiResponse<List<ResponseCpDTO>>> getCpList(
            @RequestParam(required = false) String cpSearchCategorySeq,
            @RequestParam(required = false) String cpSearchCategoryData,
            @RequestParam(required = false) String cpName) {

        logger.info("카테고리 시퀀스: {}, 카테고리 데이터: {}, 이름: {}로 CP 리스트 요청을 받았습니다.", cpSearchCategorySeq, cpSearchCategoryData, cpName);

        List<Long> cpSearchCategorySeqList = cpSearchCategorySeq != null && !cpSearchCategorySeq.isEmpty()
                ? Arrays.stream(cpSearchCategorySeq.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList()) : null;

        List<Long> cpSearchCategoryDataList = cpSearchCategoryData != null && !cpSearchCategoryData.isEmpty()
                ? Arrays.stream(cpSearchCategoryData.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList()) : null;

        try {
            List<ResponseCpDTO> cpList;

            // 카테고리 시퀀스 또는 카테고리 데이터로 검색
            if (cpSearchCategorySeqList != null || cpSearchCategoryDataList != null) {
                logger.info("카테고리 기반 검색 수행. 호출할 메서드: getCpListByCpSearchCategoryAndCpSearchCategoryData");
                cpList = cpService.getCpListByCpSearchCategoryAndCpSearchCategoryData(cpSearchCategorySeqList, cpSearchCategoryDataList);
            } else if (cpName != null && !cpName.isEmpty()) {
                logger.info("이름 기반 검색 수행. 호출할 메서드: getCpListByCpName");
                cpList = cpService.getCpListByCpName(cpName);
            } else {
                logger.info("전체 검색 수행. 호출할 메서드: getCpList");
                cpList = cpService.getCpList();
            }

            if (cpList == null || cpList.isEmpty()) {
                logger.info("검색 결과가 없습니다.");
                return ResponseEntity.ok(ApiResponse.ok(null));
            } else {
                logger.info("조회된 CP 리스트 크기: {}", cpList.size());
                return ResponseEntity.ok(ApiResponse.ok(cpList));
            }
        } catch (CustomException e) {
            logger.error("CP 리스트를 가져오는 동안 CustomException이 발생했습니다: {}", e.getMessage());
            return ResponseEntity
                    .status(e.getErrorCode().getHttpStatus())
                    .body(ApiResponse.fail(e));
        } catch (Exception e) {
            logger.error("CP 리스트를 가져오는 동안 예기치 않은 오류가 발생했습니다: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
    }

    // CP 버전 번호로 CP 조회
    @GetMapping(value = "/{cpVersionSeq}")
    @Operation(summary = "CP 조회",
            description = "주어진 CP 버전 시퀀스를 사용하여 CP를 조회합니다.")
    public ResponseEntity<ApiResponse<ResponseCpDTO>> getCpByCpVersionSeq(
            @PathVariable long cpVersionSeq,
            HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("버전 시퀀스: {}로 CP를 가져오는 요청을 받았습니다.", cpVersionSeq);

        try {
            // Cp 버전을 통하여 Cp 를 가져오는 서비스 호출
            ResponseCpDTO cpList = cpService.getCpByCpVersionSeq(cpVersionSeq, request, response);

            if (cpList == null) {
                logger.warn("버전 시퀀스 '{}'에 대한 CP 레코드가 없습니다.", cpVersionSeq);
                return ResponseEntity.ok(ApiResponse.ok(null)); // 빈 응답
            }

            logger.info("버전 시퀀스로 성공적으로 CP를 가져왔습니다: {}", cpList);
            // 성공적인 응답 반환
            return ResponseEntity.ok(ApiResponse.ok(cpList));
        } catch (CustomException e) {
            logger.error("버전 시퀀스로 CP를 가져오는 동안 CustomException이 발생했습니다: {}", e.getMessage(), e);
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

    @PostMapping(value = "/bookmark/{cpVersionSeq}")
    @Operation(summary = "주어진 CP 버전의 북마크 토글",
            description = "이 API는 지정된 CP 버전 시퀀스에 대해 사용자의 북마크 상태를 토글합니다. " +
                    "북마크가 되어 있지 않으면 북마크를 추가하고, 이미 북마크 되어 있으면 해제합니다.")
    public ResponseEntity<ApiResponse<String>> toggleBookmark(@PathVariable long cpVersionSeq) {
        logger.info("CP 버전 {}에 대한 북마크 토글 요청이 수신되었습니다.", cpVersionSeq);

        boolean isBookmarked = cpService.cpBookmark(cpVersionSeq);

        if (isBookmarked) {
            logger.info("CP 버전 {}의 북마크가 완료되었습니다.", cpVersionSeq);
            return ResponseEntity.ok(ApiResponse.ok("북마크 완료"));
        } else {
            logger.info("CP 버전 {}의 북마크가 해제되었습니다.", cpVersionSeq);
            return ResponseEntity.ok(ApiResponse.ok("북마크 해제"));
        }
    }

    @GetMapping("/mypage")
    @Operation(summary = "사용자의 북마크된 CP 버전 조회",
            description = "사용자가 북마크한 CP 버전 목록을 조회하여 반환합니다.")
    public ResponseEntity<ApiResponse<List<ResponseCpDTO>>> getBookmarkedCp() {
        logger.info("사용자의 북마크된 CP 버전 목록 조회 요청이 수신되었습니다.");

        List<ResponseCpDTO> bookmarkedCpList = cpService.getBookmarkedCp();
        logger.info("사용자의 북마크된 CP 버전 목록이 {}개 조회되었습니다.", bookmarkedCpList.size());

        return ResponseEntity.ok(ApiResponse.ok(bookmarkedCpList));
    }
}
