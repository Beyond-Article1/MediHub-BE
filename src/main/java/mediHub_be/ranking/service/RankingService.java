package mediHub_be.ranking.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.ranking.dto.RankingDTO;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.ranking.repository.RankingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    @Transactional(readOnly = true)
    public List<RankingDTO> getAllRankings() {
        return rankingRepository.findAll()
                .stream()
                .map(ranking -> RankingDTO.builder()
                        .rankingSeq(ranking.getRankingSeq())
                        .deptSeq(ranking.getDeptSeq())
                        .rankingNum(ranking.getRankingNum())
                        .rankingName(ranking.getRankingName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public RankingDTO createRanking(RankingDTO rankingDTO) {
        Ranking ranking = Ranking.builder()
                .deptSeq(rankingDTO.getDeptSeq())
                .rankingNum(rankingDTO.getRankingNum())
                .rankingName(rankingDTO.getRankingName())
                .build();

        ranking = rankingRepository.save(ranking);

        return RankingDTO.builder()
                .rankingSeq(ranking.getRankingSeq())
                .deptSeq(ranking.getDeptSeq())
                .rankingNum(ranking.getRankingNum())
                .rankingName(ranking.getRankingName())
                .build();
    }

    @Transactional
    public RankingDTO updateRanking(long rankingSeq, RankingDTO rankingDTO) {
        Ranking ranking = rankingRepository.findById(rankingSeq)
                .orElseThrow(() -> new RuntimeException("Ranking not found"));

        ranking.updateRanking(rankingDTO.getDeptSeq(), rankingDTO.getRankingNum(), rankingDTO.getRankingName());

        return RankingDTO.builder()
                .rankingSeq(ranking.getRankingSeq())
                .deptSeq(ranking.getDeptSeq())
                .rankingNum(ranking.getRankingNum())
                .rankingName(ranking.getRankingName())
                .build();
    }

    @Transactional
    public void deleteRanking(long rankingSeq) {
        if (!rankingRepository.existsById(rankingSeq)) {
            throw new RuntimeException("Ranking not found");
        }
        rankingRepository.deleteById(rankingSeq);
    }
}
