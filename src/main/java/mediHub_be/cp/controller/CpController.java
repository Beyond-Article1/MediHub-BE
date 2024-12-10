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
import mediHub_be.cp.dto.*;
import mediHub_be.cp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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
    private final CpSearchCategoryService cpSearchCategoryService;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.controller.CpController"); // Logger

    // CP 검색 조회
    // example: https://medihub.info/cp?cpSearchCategorySeq=1,2,3&cpSearchCategoryData=1,2,3
    @GetMapping
    @Operation(
            summary = "CP 리스트 조회",
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

    // CP 검색 카테고리 전체 조회
    @GetMapping(value = "/cpSearchCategory")
    @Operation(summary = "CP 검색 카테고리 조회",
            description = "모든 CP 검색 카테고리를 조회하여 리스트로 반환합니다. ")
    public ResponseEntity<ApiResponse<List<ResponseCpSearchCategoryDTO>>> getCpSearchCategoryList() {

        List<ResponseCpSearchCategoryDTO> dtoList;

        logger.info("CP 검색 카테고리 조회 요청");

        try {
            dtoList = cpSearchCategoryService.getCpSearchCategoryList();
            logger.info("CP 검색 카테고리 조회 성공: {} 카테고리", dtoList.size());
        } catch (CustomException e) {
            logger.error("CP 검색 카테고리 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR)));
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }

        return ResponseEntity.ok(ApiResponse.ok(dtoList));
    }

    // CP 검색 카테고리 상세 조회
    @GetMapping(value = "/cpSearchCategory/{cpSearchCategorySeq}")
    @Operation(summary = "CP 검색 카테고리 조회",
            description = "지정된 CP 검색 카테고리의 상세 정보를 반환합니다. " +
                    "주어진 카테고리 번호에 해당하는 정보를 조회하여, " +
                    "해당 카테고리가 존재하지 않을 경우 적절한 오류 메시지를 반환합니다.")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDTO>> getCpSearchCategory(@PathVariable long cpSearchCategorySeq) {

        ResponseCpSearchCategoryDTO entity;

        logger.info("CP 검색 카테고리 조회 요청: cpSearchCategorySeq={}", cpSearchCategorySeq);

        try {
            entity = cpSearchCategoryService.getCpSearchCategory(cpSearchCategorySeq);
            logger.info("CP 검색 카테고리 조회 성공: {}", entity);
        } catch (CustomException e) {
            logger.error("CP 검색 카테고리 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR)));
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }

        logger.info("CP 검색 카테고리 조회 결과 반환: {}", entity);
        return ResponseEntity.ok(ApiResponse.ok(entity));
    }

    @PostMapping(value = "/cpSearchCategory")
    @Operation(summary = "CP 검색 카테고리 생성", description = "제공된 이름으로 새로운 CP 검색 카테고리를 생성합니다.")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDTO>> createCpSearchCategory(@RequestBody String cpSearchCategoryName) {

        ResponseCpSearchCategoryDTO dto;

        // 요청받은 카테고리 이름 로그
        logger.info("CP 검색 카테고리 생성 요청: {}", cpSearchCategoryName);

        try {
            // 서비스 메서드를 호출하여 카테고리 생성
            dto = cpSearchCategoryService.createCpSearchCategory(cpSearchCategoryName);
            logger.info("CP 검색 카테고리 성공적으로 생성됨: {}", dto);
        } catch (CustomException e) {
            // 사용자 정의 예외 처리: 클라이언트 오류에 대한 응답
            logger.warn("CP 검색 카테고리 생성 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리: 서버 오류에 대한 응답
            logger.error("데이터베이스 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR)));
        } catch (Exception e) {
            // 기타 예외 처리: 서버 오류에 대한 응답
            logger.error("알 수 없는 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }

        // 성공적으로 생성된 경우 201 Created 응답 반환
        return ResponseEntity.ok(ApiResponse.created(dto));
    }

    // CP 검색 카테고리 수정
    @PutMapping(value = "/cpSearchCategory/{cpSearchCategorySeq}")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDTO>> updateCpSearchCategory(
            @PathVariable long cpSearchCategorySeq,
            @RequestBody String cpSearchCategoryName) {

        ResponseCpSearchCategoryDTO dto;

        try {
            dto = cpSearchCategoryService.updateCpSearchCategory(cpSearchCategorySeq, cpSearchCategoryName);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    // CP 검색 카테고리 삭제
    @DeleteMapping(value = "/cpSearchCategory/{cpSearchCategorySeq}")
    @Operation(summary = "CP 검색 카테고리 삭제", description = "주어진 ID로 CP 검색 카테고리를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteCpSearchCategory(@PathVariable long cpSearchCategorySeq) {
        try {
            cpSearchCategoryService.deleteCpSearchCategory(cpSearchCategorySeq);
            // 삭제 성공 시 응답
            return ResponseEntity.ok(ApiResponse.ok(null));
        } catch (CustomException e) {
            // 사용자 정의 예외 처리: 클라이언트 오류에 대한 응답
            logger.warn("CP 검색 카테고리 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리: 서버 오류에 대한 응답
            logger.error("데이터베이스 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR)));
        } catch (Exception e) {
            // 기타 예외 처리: 서버 오류에 대한 응답
            logger.error("알 수 없는 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
    }

    // CP 검색 카테고리 데이터 전체 조회
    @GetMapping(value = "/cpSearchCategory/{cpSearchCategorySeq}/cpSearchCategoryData")
    @Operation(summary = "CP 검색 카테고리 데이터 조회",
            description = "주어진 CP 검색 카테고리 ID에 대한 모든 데이터를 조회하여 리스트로 반환합니다.")
    public ResponseEntity<ApiResponse<List<ResponseCpSearchCategoryDataDTO>>> getCpSearchCategoryDataList(@PathVariable long cpSearchCategorySeq) {

        List<ResponseCpSearchCategoryDataDTO> dtoList;

        logger.info("CP 검색 카테고리 데이터 조회 요청: ID={}", cpSearchCategorySeq);

        try {
            // 서비스 호출하여 데이터 리스트 조회
            dtoList = cpSearchCategoryService.getCpSearchCategoryDataListByCpSearchCategorySeq(cpSearchCategorySeq);
            logger.info("CP 검색 카테고리 데이터 조회 성공: ID={} 데이터 수={}", cpSearchCategorySeq, dtoList.size());
        } catch (CustomException e) {
            logger.error("CP 검색 카테고리 데이터 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (DataAccessException e) {
            logger.error("데이터 접근 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR)));
        } catch (Exception e) {
            logger.error("예기치 않은 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }

        return ResponseEntity.ok(ApiResponse.ok(dtoList));
    }


    // CP 검색 카테고리 데이터 단일 조회
    @GetMapping(value = "/cpSearchCategory/{cpSearchCategorySeq}/cpSearchCategoryData/{cpSearchCategoryDataSeq}")
    @Operation(summary = "CP 검색 카테고리 데이터 조회",
            description = "주어진 CP 검색 카테고리 시퀀스와 CP 검색 카테고리 데이터 시퀀스를 사용하여 데이터를 조회합니다.")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDataDTO>> getCpSearchCategoryData(
            @PathVariable long cpSearchCategorySeq,
            @PathVariable long cpSearchCategoryDataSeq) {

        logger.info("CP 검색 카테고리 번호: {}, CP 검색 카테고리 데이터 번호: {}로 조회 요청했습니다.", cpSearchCategorySeq, cpSearchCategoryDataSeq);

        try {
            // CP 검색 카테고리 데이터를 가져오는 서비스 호출
            ResponseCpSearchCategoryDataDTO cpSearchCategoryData = cpSearchCategoryService.getCpSearchCategoryData(cpSearchCategorySeq, cpSearchCategoryDataSeq);

            logger.info("CP 검색 카테고리 데이터 조회 성공: {}", cpSearchCategoryData);
            return ResponseEntity.ok(ApiResponse.ok(cpSearchCategoryData));
        } catch (CustomException e) {
            logger.error("CP 검색 카테고리 데이터 조회 중 CustomException 발생: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (Exception e) {
            logger.error("CP 검색 카테고리 데이터 조회 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
    }

    @PostMapping(value = "/cpSearchCategory/{cpSearchCategorySeq}/cpSearchCategoryData")
    @Operation(summary = "CP 검색 카테고리 데이터 생성",
            description = "주어진 CP 검색 카테고리 시퀀스를 사용하여 새로운 CP 검색 카테고리 데이터를 생성합니다.")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDataDTO>> createCpSearchCategoryData(
            @PathVariable long cpSearchCategorySeq,
            @RequestBody String cpSearchCategoryDataName) {

        logger.info("CP 검색 카테고리 데이터 생성 요청: cpSearchCategorySeq={}, cpSearchCategoryDataName={}", cpSearchCategorySeq, cpSearchCategoryDataName);

        ResponseCpSearchCategoryDataDTO dto;

        try {
            // 서비스 메서드를 호출하여 카테고리 데이터 생성
            dto = cpSearchCategoryService.createCpSearchCategoryData(cpSearchCategorySeq, cpSearchCategoryDataName);
            logger.info("CP 검색 카테고리 데이터 성공적으로 생성됨: {}", dto);
        } catch (CustomException e) {
            // 사용자 정의 예외 처리: 클라이언트 오류에 대한 응답
            logger.warn("CP 검색 카테고리 데이터 생성 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리: 서버 오류에 대한 응답
            logger.error("데이터베이스 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR)));
        } catch (Exception e) {
            // 기타 예외 처리: 서버 오류에 대한 응답
            logger.error("알 수 없는 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }

        // 성공적으로 생성된 경우 201 Created 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(dto));
    }

    // CP 검색 카테고리 데이터 수정
    @PutMapping(value = "/cpSearchCategory/{cpSearchCategorySeq}/cpSearchCategoryData/{cpSearchCategoryDataSeq}")
    @Operation(summary = "CP 검색 카테고리 데이터 수정",
            description = "주어진 CP 검색 카테고리 시퀀스와 데이터 시퀀스를 사용하여 해당 카테고리 데이터를 수정합니다.")
    public ResponseEntity<ApiResponse<ResponseCpSearchCategoryDataDTO>> updateCpSearchCategoryData(
            @PathVariable long cpSearchCategorySeq,
            @PathVariable long cpSearchCategoryDataSeq,
            @RequestBody String cpSearchCategoryDataName) {

        ResponseCpSearchCategoryDataDTO dto;

        try {
            // 서비스 메서드를 호출하여 카테고리 데이터 수정
            dto = cpSearchCategoryService.updateCpSearchCategoryDataData(cpSearchCategoryDataSeq, cpSearchCategoryDataName);
        } catch (CustomException e) {
            // 사용자 정의 예외 처리: 클라이언트 오류에 대한 응답
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리: 서버 오류에 대한 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_DATA_ACCESS_ERROR)));
        } catch (Exception e) {
            // 기타 예외 처리: 서버 오류에 대한 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }

        // 성공적으로 수정된 경우 200 OK 응답 반환
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    // CP 검색 카테고리 데이터 삭제
    @DeleteMapping(value = "/cpSearchCategory/{cpSearchCategorySeq}/cpSearchCategoryData/{cpSearchCategoryDataSeq}")
    @Operation(summary = "CP 검색 카테고리 데이터 삭제",
            description = "주어진 CP 검색 카테고리 ID와 데이터 ID로 CP 검색 카테고리 데이터를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteCpSearchCategoryData(
            @PathVariable long cpSearchCategorySeq,
            @PathVariable long cpSearchCategoryDataSeq) {

        try {
            cpSearchCategoryService.deleteCpSearchCategoryData(cpSearchCategoryDataSeq);
            // 삭제 성공 시 응답
            return ResponseEntity.ok(ApiResponse.ok(null));
        } catch (CustomException e) {
            // 사용자 정의 예외 처리: 클라이언트 오류에 대한 응답
            logger.warn("CP 검색 카테고리 데이터 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ApiResponse.fail(e));
        } catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리: 서버 오류에 대한 응답
            logger.error("데이터베이스 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_DATABASE_ERROR)));
        } catch (Exception e) {
            // 기타 예외 처리: 서버 오류에 대한 응답
            logger.error("알 수 없는 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
        }
    }
}
