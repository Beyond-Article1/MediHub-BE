package mediHub_be.rank.controller;

import lombok.RequiredArgsConstructor;
import mediHub_be.rank.dto.RankDTO;
import mediHub_be.rank.service.RankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rank")
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    @GetMapping
    public ResponseEntity<List<RankDTO>> getAllRanks() {
        List<RankDTO> ranks = rankService.getAllRanks();
        return ResponseEntity.ok(ranks);
    }

    @PostMapping
    public ResponseEntity<RankDTO> createRank(@RequestBody RankDTO rankDTO) {
        RankDTO createdRank = rankService.createRank(rankDTO);
        return ResponseEntity.ok(createdRank);
    }

    @PutMapping("/{rankSeq}")
    public ResponseEntity<RankDTO> updateRank(@PathVariable long rankSeq, @RequestBody RankDTO rankDTO) {
        RankDTO updatedRank = rankService.updateRank(rankSeq, rankDTO);
        return ResponseEntity.ok(updatedRank);
    }

    @DeleteMapping("/{rankSeq}")
    public ResponseEntity<Void> deleteRank(@PathVariable long rankSeq) {
        rankService.deleteRank(rankSeq);
        return ResponseEntity.noContent().build();
    }
}

