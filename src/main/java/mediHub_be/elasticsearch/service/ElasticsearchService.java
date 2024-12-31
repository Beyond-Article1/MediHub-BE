package mediHub_be.elasticsearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.anonymousBoard.entity.AnonymousBoard;
import mediHub_be.anonymousBoard.repository.AnonymousBoardRepository;
import mediHub_be.board.entity.Keyword;
import mediHub_be.board.service.KeywordService;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.repository.CpVersionRepository;
import mediHub_be.elasticsearch.document.*;
import mediHub_be.elasticsearch.repository.*;
import mediHub_be.journal.entity.Journal;
import mediHub_be.journal.repository.JournalRepository;
import mediHub_be.medicalLife.entity.MedicalLife;
import mediHub_be.medicalLife.repository.MedicalLifeRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@ConditionalOnProperty(
        name = "spring.data.elasticsearch.repositories.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class ElasticsearchService {

    private final AnonymousBoardElasticRepository anonymousBoardElasticRepository;
    private final AnonymousBoardRepository anonymousBoardRepository;
    private final CaseSharingElasticRepository caseSharingElasticRepository;
    private final CaseSharingRepository caseSharingRepository;
    private final CpElasticRepository cpElasticRepository;
    private final CpVersionRepository cpVersionRepository;
    private final JournalElasticRepository journalElasticRepository;
    private final JournalRepository journalRepository;
    private final MedicalLifeElasticRepository medicalLifeElasticRepository;
    private final MedicalLifeRepository medicalLifeRepository;
    private final KeywordService keywordService;

    public List<AnonymousBoardDocument> findAnonymousBoard(String findAnonymousBoardTitle) {

        return anonymousBoardElasticRepository.findByAnonymousBoardTitle(findAnonymousBoardTitle);
    }

    public List<String> queryAnonymousBoard(String queryAnonymousBoardTitle) {

        return anonymousBoardElasticRepository
                .findAnonymousBoardDocumentByKeywordInField(
                        "anonymous_board_title",
                        queryAnonymousBoardTitle
                ).stream()
                .map(AnonymousBoardDocument::getAnonymousBoardTitle)
                .toList();
    }

    // DB 데이터를 엘라스틱서치에 동기화
    @Transactional
    public void indexAnonymousBoard() {

        // DB에서 모든 익명 게시판 데이터 조회
        List<AnonymousBoard> anonymousBoardList = anonymousBoardRepository.findAll();

        // AnonymousBoardDocument로 변환
        List<AnonymousBoardDocument> anonymousBoardDocumentList = anonymousBoardList.stream()
                .map(anonymousBoard -> {

                    List<Keyword> keywordList = keywordService.getKeywordList(
                            "ANONYMOUS_BOARD",
                            anonymousBoard.getAnonymousBoardSeq()
                    );

                    return AnonymousBoardDocument.from(anonymousBoard, keywordList);
                })
                .collect(Collectors.toList());

        // 엘라스틱서치에 저장
        anonymousBoardElasticRepository.saveAll(anonymousBoardDocumentList);
    }

    public List<CaseSharingDocument> findCaseSharing(String findCaseSharingTitle) {

        return caseSharingElasticRepository.findByCaseSharingTitle(findCaseSharingTitle);
    }

    public List<String> queryCaseSharing(String queryCaseSharingTitle) {

        return caseSharingElasticRepository
                .findCaseSharingDocumentByKeywordInField(
                        "case_sharing_title",
                        queryCaseSharingTitle
                ).stream()
                .map(CaseSharingDocument::getCaseSharingTitle)
                .toList();
    }

    // DB 데이터를 엘라스틱서치에 동기화
    @Transactional
    public void indexCaseSharing() {

        // DB에서 모든 케이스 공유 데이터 조회
        List<CaseSharing> caseSharingList = caseSharingRepository.findAll();

        // CaseSharingDocument로 변환
        List<CaseSharingDocument> caseSharingDocumentList = caseSharingList.stream()
                .map(caseSharing -> {

                    List<Keyword> keywordList = keywordService.getKeywordList(
                            "CASE_SHARING",
                            caseSharing.getCaseSharingSeq()
                    );

                    return CaseSharingDocument.from(caseSharing, keywordList);
                })
                .collect(Collectors.toList());

        // 엘라스틱서치에 저장
        caseSharingElasticRepository.saveAll(caseSharingDocumentList);
    }

    public List<CpDocument> findCp(String findCpName) {

        return cpElasticRepository.findByCpName(findCpName);
    }

    public List<String> queryCp(String queryCpName) {

        return cpElasticRepository
                .findCpDocumentByKeywordInField(
                        "cp_name",
                        queryCpName
                ).stream()
                .map(CpDocument::getCpName)
                .toList();
    }

    // DB 데이터를 엘라스틱서치에 동기화
    @Transactional
    public void indexCp() {

        // DB에서 모든 CP 데이터 조회
        List<ResponseCpDTO> responseCpDTOList = cpVersionRepository.findLatestCp();

        // CpDocument로 변환
        List<CpDocument> cpDocumentList = responseCpDTOList.stream()
                .map(CpDocument::from)
                .toList();

        // 엘라스틱서치에 저장
        cpElasticRepository.saveAll(cpDocumentList);
    }

    public List<JournalDocument> findJournal(String findJournalKoreanTitle) {

        return journalElasticRepository.findByJournalKoreanTitle(findJournalKoreanTitle);
    }

    public List<String> queryJournal(String queryJournalKoreanTitle) {

        return journalElasticRepository
                .findJournalDocumentByKeywordInField(
                        "journal_korean_title",
                        queryJournalKoreanTitle
                ).stream()
                .map(JournalDocument::getJournalKoreanTitle)
                .toList();
    }

    // DB 데이터를 엘라스틱서치에 동기화
    @Transactional
    public void indexJournal() {

        // DB에서 모든 논문 데이터 조회
        List<Journal> journalList = journalRepository.findAll();

        // JournalDocument로 변환
        List<JournalDocument> journalDocumentList = journalList.stream()
                .map(JournalDocument::from)
                .toList();

        // 엘라스틱서치에 저장
        journalElasticRepository.saveAll(journalDocumentList);
    }

    public List<MedicalLifeDocument> findMedicalLife(String findMedicalLifeTitle) {

        return medicalLifeElasticRepository.findByMedicalLifeTitle(findMedicalLifeTitle);
    }

    public List<String> queryMedicalLife(String queryMedicalLifeTitle) {

        return medicalLifeElasticRepository
                .findMedicalLifeDocumentByKeywordInField(
                        "medical_life_title",
                        queryMedicalLifeTitle
                ).stream()
                .map(MedicalLifeDocument::getMedicalLifeTitle)
                .toList();
    }

    // DB 데이터를 엘라스틱서치에 동기화
    @Transactional
    public void indexMedicalLife() {

        // DB에서 모든 메디컬 라이프 데이터 조회
        List<MedicalLife> medicalLifeList = medicalLifeRepository.findAll();

        // MedicalLifeDocument로 변환
        List<MedicalLifeDocument> medicalLifeDocumentList = medicalLifeList.stream()
                .map(medicalLife -> {

                    List<Keyword> keywordList = keywordService.getKeywordList(
                            "MEDICAL_LIFE",
                            medicalLife.getMedicalLifeSeq()
                    );

                    return MedicalLifeDocument.from(medicalLife, keywordList);
                })
                .collect(Collectors.toList());

        // 엘라스틱서치에 저장
        medicalLifeElasticRepository.saveAll(medicalLifeDocumentList);
    }
}