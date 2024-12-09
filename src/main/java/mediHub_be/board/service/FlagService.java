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

    // flagType
    public static final String CP_OPINION_BOARD_FLAG = "cp_opinion";

    // Flag 조회 (존재하지 않으면 빈 Optional 반환)
    @Transactional(readOnly = true)
    public Optional<Flag> findFlag(String flagType, Long entitySeq) {
        return flagRepository.findByFlagTypeAndFlagEntitySeq(flagType, entitySeq);
    }

    public void deleteFlag(Long flagSeq) {
        flagRepository.deleteById(flagSeq);
    }
}

