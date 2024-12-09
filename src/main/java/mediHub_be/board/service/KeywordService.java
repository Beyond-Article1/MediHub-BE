package mediHub_be.board.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.entity.Keyword;
import mediHub_be.board.repository.FlagRepository;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.case_sharing.dto.CaseSharingKeywordDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final FlagService flagService;

    // 키워드 저장
    @Transactional
    public void saveKeywords(List<String> keywords, Long flagSeq) {
        // Keyword 저장
        for (String keywordName : keywords) {
            Keyword keyword = Keyword.builder()
                    .flagSeq(flagSeq)
                    .keywordName(keywordName)
                    .build();
            keywordRepository.save(keyword);
        }
    }


    // 키워드 수정
    @Transactional
    public void updateKeywords(List<String> newKeywords, String boardFlag, Long postSeq) {
        // Flag 가져오기
        Flag flag = flagService.findFlag(boardFlag, postSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 Flag가 존재하지 않습니다."));

        // 기존 키워드 삭제
        keywordRepository.deleteByFlagSeq(flag.getFlagSeq());

        // 새 키워드 저장
        for (String keywordName : newKeywords) {
            Keyword keyword = Keyword.builder()
                    .flagSeq(flag.getFlagSeq())
                    .keywordName(keywordName)
                    .build();
            keywordRepository.save(keyword);
        }
    }

    // 키워드 삭제
    @Transactional
    public void deleteKeywords(String boardFlag, Long postSeq) {
        // Flag 가져오기
        Flag flag = flagService.findFlag(boardFlag, postSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 Flag가 존재하지 않습니다."));

        // 키워드 삭제
        keywordRepository.deleteByFlagSeq(flag.getFlagSeq());
        flagService.deleteFlag(flag.getFlagSeq());
    }

    // 특정 게시물의 키워드 조회
    @Transactional
    public List<CaseSharingKeywordDTO> getKeywords(String boardFlag, Long postSeq) {
        List<Keyword> keywords = keywordRepository.findByBoardFlagAndPostSeq(boardFlag, postSeq);

        return keywords.stream()
                .map(keyword -> new CaseSharingKeywordDTO(
                        keyword.getKeywordSeq(),
                        keyword.getKeywordName()
                ))
                .collect(Collectors.toList());
    }
}

