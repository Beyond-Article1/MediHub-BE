package mediHub_be.ranking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.ranking.dto.RankingDTO;
import mediHub_be.ranking.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
@Tag(name = "직급", description = "직급 API")
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "직급 조회", description = "직급 조회" )
    @GetMapping
    public ResponseEntity<ApiResponse<List<RankingDTO>>> getAllRankings() {
        List<RankingDTO> rankings = rankingService.getAllRankings();
        return ResponseEntity.ok(ApiResponse.ok(rankings));
    }

    @Operation(summary = "직급 등록", description = "직급 등록" )
    @PostMapping
    public ResponseEntity<ApiResponse<RankingDTO>> createRanking(@RequestBody RankingDTO rankingDTO) {
        RankingDTO createdRanking = rankingService.createRanking(rankingDTO);
        return ResponseEntity.ok(ApiResponse.ok(createdRanking));
    }

    @Operation(summary = "직급 수정", description = "직급 수정" )
    @PutMapping("/{rankingSeq}")
    public ResponseEntity<ApiResponse<RankingDTO>> updateRanking(
            @PathVariable long rankingSeq,
            @RequestBody RankingDTO rankingDTO) {
        RankingDTO updatedRanking = rankingService.updateRanking(rankingSeq, rankingDTO);
        return ResponseEntity.ok(ApiResponse.ok(updatedRanking));
    }

    @Operation(summary = "직급 삭제", description = "직급 삭제" )
    @DeleteMapping("/{rankingSeq}")
    public ResponseEntity<ApiResponse<Long>> deleteRanking(@PathVariable long rankingSeq) {
        rankingService.deleteRanking(rankingSeq);
        return ResponseEntity.ok(ApiResponse.ok(rankingSeq));
    }

    @Operation(summary = "부서별 직급 조회", description = "특정 부서에 해당하는 직급을 조회합니다.")
    @GetMapping("/by-dept/{deptSeq}")
    public ResponseEntity<ApiResponse<List<RankingDTO>>> getRankingsByDeptSeq(@PathVariable Long deptSeq) {
        List<RankingDTO> rankings = rankingService.getRankingsByDeptSeq(deptSeq);
        return ResponseEntity.ok(ApiResponse.ok(rankings));
    }
}
