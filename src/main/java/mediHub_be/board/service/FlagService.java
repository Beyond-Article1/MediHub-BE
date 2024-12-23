package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.FlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlagService {

    private final FlagRepository flagRepository;

    // Flag 목록 조회
    @Transactional(readOnly = true)
    public List<Flag> findAllFlag() {

        return flagRepository.findAll();
    }

    // Flag 조회 (존재하지 않으면 빈 Optional 반환)
    @Transactional(readOnly = true)
    public Optional<Flag> findFlag(String flagType, Long entitySeq) {
        return flagRepository.findByFlagTypeAndFlagEntitySeq(flagType, entitySeq);
    }

    // 게시판 식별 생성 or 있는 식별 반환
    @Transactional
    public Flag createFlag(String flagType, Long entitySeq) {

        return flagRepository.findByFlagTypeAndFlagEntitySeq(flagType, entitySeq)
                .orElseGet(() -> flagRepository.save(Flag.builder()
                        .flagType(flagType)
                        .flagEntitySeq(entitySeq)
                        .build()));
    }

    public void deleteFlag(Long flagSeq) {
        flagRepository.deleteById(flagSeq);
    }
}