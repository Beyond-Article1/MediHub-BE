package mediHub_be.ranking.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.ranking.dto.RankingDTO;
import mediHub_be.ranking.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    public ResponseEntity<List<RankingDTO>> getAllRankings() {
        List<RankingDTO> rankings = rankingService.getAllRankings();
        return ResponseEntity.ok(rankings);
    }

    @PostMapping
    public ResponseEntity<RankingDTO> createRanking(@RequestBody RankingDTO rankingDTO) {
        RankingDTO createdRanking = rankingService.createRanking(rankingDTO);
        return ResponseEntity.ok(createdRanking);
    }

    @PutMapping("/{rankingSeq}")
    public ResponseEntity<RankingDTO> updateRanking(@PathVariable long rankingSeq, @RequestBody RankingDTO rankingDTO) {
        RankingDTO updatedRanking = rankingService.updateRanking(rankingSeq, rankingDTO);
        return ResponseEntity.ok(updatedRanking);
    }

    @DeleteMapping("/{rankingSeq}")
    public ResponseEntity<Void> deleteRanking(@PathVariable long rankingSeq) {
        rankingService.deleteRanking(rankingSeq);
        return ResponseEntity.noContent().build();
    }
}
