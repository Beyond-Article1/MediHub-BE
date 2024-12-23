package mediHub_be.ranking.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
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

    // 직급 조회
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

    // 직급 등록
    @Transactional
    public RankingDTO createRanking(RankingDTO rankingDTO) {
        Ranking ranking = Ranking.builder()
                .deptSeq(rankingDTO.getDeptSeq())
                .rankingNum(rankingDTO.getRankingNum())
                .rankingName(rankingDTO.getRankingName())
                .build();

        Ranking savedRanking = rankingRepository.save(ranking);

        return RankingDTO.builder()
                .rankingSeq(savedRanking.getRankingSeq())
                .deptSeq(savedRanking.getDeptSeq())
                .rankingNum(savedRanking.getRankingNum())
                .rankingName(savedRanking.getRankingName())
                .build();
    }

    // 직급 수정
    @Transactional
    public RankingDTO updateRanking(long rankingSeq, RankingDTO rankingDTO) {
        Ranking ranking = rankingRepository.findById(rankingSeq)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RANKING));

        ranking.updateRanking(rankingDTO.getDeptSeq(), rankingDTO.getRankingNum(), rankingDTO.getRankingName());

        return RankingDTO.builder()
                .rankingSeq(ranking.getRankingSeq())
                .deptSeq(ranking.getDeptSeq())
                .rankingNum(ranking.getRankingNum())
                .rankingName(ranking.getRankingName())
                .build();
    }

    // 직급 삭제
    @Transactional
    public void deleteRanking(long rankingSeq) {
        if (!rankingRepository.existsById(rankingSeq)) {
            throw new CustomException(ErrorCode.NOT_FOUND_RANKING);
        }
        rankingRepository.deleteById(rankingSeq);
    }
}
