package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Keyword;
import mediHub_be.board.repository.KeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final FlagService flagService;
    private final KeywordRepository keywordRepository;

    @Transactional
    public void saveKeywords(List<String> keywords, String boardFlag, Long postSeq) {
        // Flag 저장
        Flag flag = flagService.saveFlag(boardFlag, postSeq);

        // Keyword 저장
        for (String keywordName : keywords) {
            Keyword keyword = Keyword.builder()
                    .flagSeq(flag.getFlagSeq()) // 저장된 Flag의 ID를 참조
                    .keywordName(keywordName)
                    .keywordPostSeq(postSeq)
                    .build();
            keywordRepository.save(keyword);
        }
    }
}
