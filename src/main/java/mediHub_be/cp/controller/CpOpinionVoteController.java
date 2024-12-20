package mediHub_be.cp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.cp.dto.CpOpinionVoteDTO;
import mediHub_be.cp.service.CpOpinionVoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "cp/{cpVersionSeq}/cpOpinionLocation/{cpOpinionLocationSeq}/{cpOpinionSeq}/cpOpinionVote")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CP", description = "CP 관련 API")
public class CpOpinionVoteController {

    // Service
    private final CpOpinionVoteService cpOpinionVoteService;

    private final Logger logger = LoggerFactory.getLogger("mediHub_be.cp.controller.CpOpinionVoteController"); // Logger

    // CP 의견 투표
    @PostMapping
    @Operation(summary = "CP 의견 투표 생성")
    public ResponseEntity<ApiResponse<CpOpinionVoteDTO>> createCpOpinionVote(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpOpinionLocationSeq,
            @PathVariable long cpOpinionSeq,
            @RequestBody boolean cpOpinionVote) {

        logger.info("CP 의견 번호: {}로 CP 의견 투표 요청을 받았습니다. 의견(true: 찬성, false: 반대): {}", cpOpinionSeq, cpOpinionVote);

        CpOpinionVoteDTO dto = cpOpinionVoteService.createCpOpinionVote(cpOpinionSeq, cpOpinionVote);
        logger.info("CP 의견 투표가 성공적으로 생성되었습니다. 투표 ID: {}", dto.getCpOpinionVoteSeq());

        return ResponseEntity.ok(ApiResponse.created(dto));
    }

    // CP 의견 투표 취소
    @DeleteMapping(value = "/{cpOpinionVoteSeq}")
    @Operation(summary = "CP 의견 투표 취소")
    public ResponseEntity<ApiResponse<Void>> deleteCpOpinionVote(
            @PathVariable long cpVersionSeq,
            @PathVariable long cpOpinionVoteSeq) {

        logger.info("CP 의견 투표 취소 요청. CP 버전 ID: {}, 투표 ID: {}", cpVersionSeq, cpOpinionVoteSeq);

        cpOpinionVoteService.deleteCpOpinionVote(cpOpinionVoteSeq);
        logger.info("CP 의견 투표가 성공적으로 취소되었습니다. 투표 ID: {}", cpOpinionVoteSeq);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
