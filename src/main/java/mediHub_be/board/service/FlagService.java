package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.FlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlagService {

    private final FlagRepository flagRepository;

    // boardFlag
    public static final String CP_OPINION_BOARD_FLAG = "cp_opinion";

    // Flag 가져오기 (없으면 생성)
    @Transactional
    public Flag saveFlag(String boardFlag, Long postSeq) {
        return flagRepository.findByFlagBoardFlagAndFlagPostSeq(boardFlag, postSeq)
                .orElseGet(() -> flagRepository.save(Flag.builder()
                        .flagBoardFlag(boardFlag)
                        .flagPostSeq(postSeq)
                        .build()));
    }

    // Flag 조회 (존재하지 않으면 빈 Optional 반환)
    @Transactional(readOnly = true)
    public Optional<Flag> findFlag(String boardFlag, Long postSeq) {
        return flagRepository.findByFlagBoardFlagAndFlagPostSeq(boardFlag, postSeq);
    }

    public void deleteFlag(Long flagSeq) {
        flagRepository.deleteById(flagSeq);
    }
}

