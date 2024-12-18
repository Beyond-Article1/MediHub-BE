package mediHub_be.journal.service;

import mediHub_be.common.response.JournalResponse;
import mediHub_be.journal.dto.ResponseAbstractDTO;
import mediHub_be.journal.dto.ResponseJournalRankDTO;
import mediHub_be.journal.dto.ResponsePubmedDTO;

import java.util.List;

public interface JournalService {

    /**
     * GPT를 통해 자연어를 처리하고 논문 검색에 해당하느 키워드를 뽑아내는 메서드
     */
    JournalResponse<?> getPubmedKeywords(String naturalRequest);

    /**
     * PMID를 기반으로 GPT를 통해 논문의 초록을 요약 해주는 메서드
     */
    ResponseAbstractDTO summarizeAbstractByPmid(Long userSeq, String journalPmid, ResponsePubmedDTO requestDTO);

    /**
     * 논문들 TOP 100 조회 (조회순, 북마크순)
     */
    List<ResponseJournalRankDTO> getJournalTop100(String sortBy, Long userSeq);
}
