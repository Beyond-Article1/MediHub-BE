package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.CpOpinionDTO;
import mediHub_be.cp.dto.RequestCpOpinionDTO;
import mediHub_be.cp.dto.ResponseCpOpinionDTO;
import mediHub_be.cp.service.CpOpinionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseEntity<ApiResponse<List<ResponseCpOpinionDTO>>> getCpOpinionListByCpOpinionLocationSeq(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpOpinionLocationSeq,
            @RequestParam(required = false, defaultValue = "false") boolean isDeleted) {

        logger.info("CP 버전 시퀀스: {}의 CP 의견 위치 시퀀스: {}에 위치의 CP 의견 리스트 요청을 받았습니다.", cpVersionSeq, cpOpinionLocationSeq);

        List<ResponseCpOpinionDTO> cpOpinionList = cpOpinionService.getCpOpinionListByCpVersionSeq(cpVersionSeq, cpOpinionLocationSeq, isDeleted);

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
            @RequestPart("data") RequestCpOpinionDTO requestBody) {

        logger.info("CP 의견 생성 요청: cpVersionSeq = {}, cpOpinionLocationSeq = {}, 요청 본문 = {}", cpVersionSeq, cpOpinionLocationSeq, requestBody);

        CpOpinionDTO cpOpinion = cpOpinionService.createCpOpinion(cpVersionSeq, cpOpinionLocationSeq, requestBody);

        logger.info("CP 의견이 성공적으로 생성되었습니다. CP 의견 정보: {}", cpOpinion);
        return ResponseEntity.ok(ApiResponse.created(cpOpinion));
    }

    // CP 의견 삭제
    @DeleteMapping(value = "/{cpOpinionLocationSeq}/{cpOpinionSeq}")
    @Operation(summary = "CP 의견 삭제",
            description = "주어진 CP 버전 시퀀스와 CP 의견 위치, CP 의견 번호를 사용하여 CP 의견을 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> deleteCpOpinionByCpOpinionSeq(
            @PathVariable String cpVersionSeq,
            @PathVariable String cpOpinionLocationSeq,
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
            @RequestPart("data") RequestCpOpinionDTO requestBody) {

        logger.info("CP 의견 수정 요청: cpOpinionLocationSeq = {}, cpOpinionSeq = {}, requestBody = {}", cpOpinionLocationSeq, cpOpinionSeq, requestBody);

        CpOpinionDTO cpOpinionDTO = cpOpinionService.updateCpOpinionByCpOpinionSeq(cpOpinionSeq, requestBody);

        logger.info("CP 의견이 성공적으로 수정되었습니다: {}", cpOpinionDTO);
        return ResponseEntity.ok(ApiResponse.ok(cpOpinionDTO));
    }

    // CP 의견 북마크
    @PostMapping(value = "/{cpOpinionLocationSeq}/bookmark/{cpOpinionSeq}")
    @Operation(summary = "CP 의견 북마크 토글",
            description = "지정된 CP 의견에 대한 북마크 상태를 토글합니다. " +
                    "북마크가 되어 있지 않으면 북마크를 추가하고, 이미 북마크 되어 있으면 해제합니다.")
    public ResponseEntity<ApiResponse<String>> toggleBookmark(
            @PathVariable long cpOpinionSeq) {
        logger.info("CP 의견 {}에 대한 북마크 토글 요청이 수신되었습니다.", cpOpinionSeq);

        boolean isBookmarked = cpOpinionService.cpOpinionBookmark(cpOpinionSeq);

        if (isBookmarked) {
            logger.info("CP 의견 {}의 북마크가 완료되었습니다.", cpOpinionSeq);
            return ResponseEntity.ok(ApiResponse.ok("북마크 완료"));
        } else {
            logger.info("CP 의견 {}의 북마크가 해제되었습니다.", cpOpinionSeq);
            return ResponseEntity.ok(ApiResponse.ok("북마크 해제"));
        }
    }

    // CP 의견 북마크 조회
    @GetMapping("/{cpOpinionLocationSeq}/mypage")
    @Operation(summary = "사용자의 북마크된 CP 의견 조회",
            description = "사용자가 북마크한 CP 의견 목록을 조회하여 반환합니다.")
    public ResponseEntity<ApiResponse<List<ResponseCpOpinionDTO>>> getBookmarkedCp() {
        logger.info("사용자의 북마크된 CP 의견 목록 조회 요청이 수신되었습니다.");

        List<ResponseCpOpinionDTO> bookmarkedCpOpinionList = cpOpinionService.getBookmarkedCpOpinion();
        logger.info("사용자의 북마크된 CP 의견 목록이 {}개 조회되었습니다.", bookmarkedCpOpinionList.size());

        return ResponseEntity.ok(ApiResponse.ok(bookmarkedCpOpinionList));
    }

    // 작성한 CP 의견 조회
    @GetMapping("/myOpinion")
    @Operation(summary = "사용자가 작성한 CP 의견 조회",
            description = "사용자가 작성한 CP 의견 리스트를 조회합니다.")
    public ResponseEntity<ApiResponse<List<ResponseCpOpinionDTO>>> getMyCpOpinionList(@PathVariable String cpVersionSeq) {
        logger.info("사용자가 작성한 CP 의견 조회 요청이 수신되었습니다.");

        List<ResponseCpOpinionDTO> dtoList = cpOpinionService.getMyCpOpinionList();
        logger.info("사용자가 작성한 CP 의견 조회에 성공하였습니다. 조회된 크기: {}", dtoList.size());

        return ResponseEntity.ok(ApiResponse.ok(dtoList));
    }
}
