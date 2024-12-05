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

        // Keyword 저장 : flag에 받은 값 저장 후 keyword 저장
        for (String keywordName : keywords) {
            Keyword keyword = Keyword.builder()
                    .flagSeq(flag.getFlagSeq()) // 저장된 Flag의 ID를 참조
                    .keywordName(keywordName)
                    .build();
            keywordRepository.save(keyword);
        }
    }

    @Transactional
    public void deleteKeywords(String boardFlag, Long postSeq) {
        // 키워드 삭제
        keywordRepository.deleteByBoardFlagAndPostSeq(boardFlag, postSeq);
    }

    @Transactional
    public void updateKeywords(List<String> keywords, String boardFlag, Long postSeq) {
        // 기존 키워드 삭제
        deleteKeywords(boardFlag, postSeq);

        // 새 키워드 저장
        saveKeywords(keywords, boardFlag, postSeq);
    }
}
