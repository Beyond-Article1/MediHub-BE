package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.repository.FlagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FlagService {

    private final FlagRepository flagRepository;

    @Transactional
    public Flag saveFlag(String boardFlag, Long postSeq) {
        Flag flag = Flag.builder()
                .flagBoardFlag(boardFlag)
                .flagPostSeq(postSeq)
                .build();
        return flagRepository.save(flag); // 저장 후 생성된 Flag 객체 반환
    }
}
