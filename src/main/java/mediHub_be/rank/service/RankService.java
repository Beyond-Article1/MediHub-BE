package mediHub_be.rank.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.rank.dto.RankDTO;
import mediHub_be.rank.entity.Rank;
import mediHub_be.rank.repository.RankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankService {

    private final RankRepository rankRepository;

    @Transactional(readOnly = true)
    public List<RankDTO> getAllRanks() {
        return rankRepository.findAll()
                .stream()
                .map(rank -> RankDTO.builder()
                        .rankSeq(rank.getRankSeq())
                        .deptSeq(rank.getDeptSeq())
                        .rankRanking(rank.getRankRanking())
                        .rankName(rank.getRankName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public RankDTO createRank(RankDTO rankDTO) {
        Rank rank = Rank.builder()
                .deptSeq(rankDTO.getDeptSeq())
                .rankRanking(rankDTO.getRankRanking())
                .rankName(rankDTO.getRankName())
                .build();

        rank = rankRepository.save(rank);

        return RankDTO.builder()
                .rankSeq(rank.getRankSeq())
                .deptSeq(rank.getDeptSeq())
                .rankRanking(rank.getRankRanking())
                .rankName(rank.getRankName())
                .build();
    }

    @Transactional
    public RankDTO updateRank(long rankSeq, RankDTO rankDTO) {
        Rank rank = rankRepository.findById(rankSeq)
                .orElseThrow(() -> new RuntimeException("Rank not found"));

        rank.updateRank(rankDTO.getDeptSeq(), rankDTO.getRankRanking(), rankDTO.getRankName());

        return RankDTO.builder()
                .rankSeq(rank.getRankSeq())
                .deptSeq(rank.getDeptSeq())
                .rankRanking(rank.getRankRanking())
                .rankName(rank.getRankName())
                .build();
    }

    @Transactional
    public void deleteRank(long rankSeq) {
        if (!rankRepository.existsById(rankSeq)) {
            throw new RuntimeException("Rank not found");
        }
        rankRepository.deleteById(rankSeq);
    }
}
