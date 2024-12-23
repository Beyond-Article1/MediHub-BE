package mediHub_be.elasticsearch.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.anonymousBoard.entity.AnonymousBoard;
import mediHub_be.anonymousBoard.repository.AnonymousBoardRepository;
import mediHub_be.board.entity.Keyword;
import mediHub_be.board.repository.KeywordRepository;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.case_sharing.repository.CaseSharingRepository;
import mediHub_be.cp.entity.Cp;
import mediHub_be.cp.repository.CpRepository;
import mediHub_be.elasticsearch.document.*;
import mediHub_be.elasticsearch.repository.*;
import mediHub_be.journal.entity.Journal;
import mediHub_be.journal.repository.JournalRepository;
import mediHub_be.medicalLife.entity.MedicalLife;
import mediHub_be.medicalLife.repository.MedicalLifeRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ElasticsearchService {

    private final AnonymousBoardElasticRepository anonymousBoardElasticRepository;
    private final AnonymousBoardRepository anonymousBoardRepository;
    private final CaseSharingFindRepository caseSharingFindRepository;
    private final CaseSharingRepository caseSharingRepository;
    private final CpFindRepository cpFindRepository;
    private final CpRepository cpRepository;
    private final JournalFindRepository journalFindRepository;
    private final JournalRepository journalRepository;
    private final MedicalLifeFindRepository medicalLifeFindRepository;
    private final MedicalLifeRepository medicalLifeRepository;
    private final UserFindRepository userFindRepository;
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;

    public List<AnonymousBoardDocument> findAnonymousBoard(String findAnonymousBoardTitle) {

        return anonymousBoardElasticRepository.findByKeywordInField(
                "anonymous_board_title",
                findAnonymousBoardTitle
        );
    }

    public List<String> queryAnonymousBoard(String queryAnonymousBoardTitle) {

        return anonymousBoardElasticRepository
                .findByKeywordInField("anonymous_board_title", queryAnonymousBoardTitle).stream()
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

                    List<Keyword> keywords = keywordRepository.findByFlagTypeAndEntitySeq(
                            "ANONYMOUS_BOARD",
                            anonymousBoard.getAnonymousBoardSeq()
                    );

                    return AnonymousBoardDocument.from(anonymousBoard, keywords);
                })
                .collect(Collectors.toList());

        // 엘라스틱서치에 저장
        anonymousBoardElasticRepository.saveAll(anonymousBoardDocumentList);
    }

    // 엘라스틱서치로 케이스 공유 검색
    public List<CaseSharingDocument> findCaseSharing(String findCaseSharingTitle) {

        return caseSharingFindRepository.findByCaseSharingTitle(findCaseSharingTitle);
    }

    // DB 데이터를 엘라스틱서치에 동기화
    @Transactional
    public void indexCaseSharing() {

        // DB에서 모든 케이스 공유 데이터 조회
        List<CaseSharing> caseSharingList = caseSharingRepository.findAll();

        // CaseSharingDocument로 변환
        List<CaseSharingDocument> caseSharingDocumentList = caseSharingList.stream()
                .map(CaseSharingDocument::from)
                .collect(Collectors.toList());

        // 엘라스틱서치에 저장
        caseSharingFindRepository.saveAll(caseSharingDocumentList);
    }

    // 엘라스틱서치로 CP 검색
    public List<CpDocument> findCp(String findCpName) {

        return cpFindRepository.findByCpName(findCpName);
    }

    // DB 데이터를 엘라스틱서치에 동기화
    @Transactional
    public void indexCp() {

        // DB에서 모든 CP 데이터 조회
        List<Cp> cpList = cpRepository.findAll();

        // CpDocument로 변환
        List<CpDocument> cpDocumentList = cpList.stream()
                .map(CpDocument::from)
                .collect(Collectors.toList());

        // 엘라스틱서치에 저장
        cpFindRepository.saveAll(cpDocumentList);
    }

    // 엘라스틱서치로 논문 검색
    public List<JournalDocument> findJournal(String findJournalKoreanTitle) {

        return journalFindRepository.findByJournalKoreanTitle(findJournalKoreanTitle);
    }

    // DB 데이터를 엘라스틱서치에 동기화
    @Transactional
    public void indexJournal() {

        // DB에서 모든 논문 데이터 조회
        List<Journal> journalList = journalRepository.findAll();

        // JournalDocument로 변환
        List<JournalDocument> journalDocumentList = journalList.stream()
                .map(JournalDocument::from)
                .collect(Collectors.toList());

        // 엘라스틱서치에 저장
        journalFindRepository.saveAll(journalDocumentList);
    }

    // 엘라스틱서치로 메디컬 라이프 검색
    public List<MedicalLifeDocument> findMedicalLife(String findMedicalLifeTitle) {

        return medicalLifeFindRepository.findByMedicalLifeTitle(findMedicalLifeTitle);
    }

    // DB 데이터를 엘라스틱서치에 동기화
    @Transactional
    public void indexMedicalLife() {

        // DB에서 모든 메디컬 라이프 데이터 조회
        List<MedicalLife> medicalLifeList = medicalLifeRepository.findAll();

        // MedicalLifeDocument로 변환
        List<MedicalLifeDocument> medicalLifeDocumentList = medicalLifeList.stream()
                .map(MedicalLifeDocument::from)
                .collect(Collectors.toList());

        // 엘라스틱서치에 저장
        medicalLifeFindRepository.saveAll(medicalLifeDocumentList);
    }

    // 엘라스틱서치로 직원 검색
    public List<UserDocument> findUser(String findUserName) {

        return userFindRepository.findByUserName(findUserName);
    }

    // DB 데이터를 엘라스틱서치에 동기화
    @Transactional
    public void indexUser() {

        // DB에서 모든 직원 데이터 조회
        List<User> userList = userRepository.findAll();

        // UserDocument로 변환
        List<UserDocument> userDocumentList = userList.stream()
                .map(UserDocument::from)
                .collect(Collectors.toList());

        // 엘라스틱서치에 저장
        userFindRepository.saveAll(userDocumentList);
    }
}