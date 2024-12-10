package mediHub_be.journal.service;

import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.entity.Flag;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.FlagService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.journal.dto.ResponseAbstractDTO;
import mediHub_be.journal.dto.ResponseJournalLogDTO;
import mediHub_be.journal.dto.ResponseJournalSearchDTO;
import mediHub_be.journal.dto.ResponsePubmedDTO;
import mediHub_be.journal.entity.Journal;
import mediHub_be.journal.entity.JournalSearch;
import mediHub_be.journal.repository.JournalRepository;
import mediHub_be.journal.repository.JournalSearchRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;

@Slf4j
@Service
public class JournalServiceImpl implements JournalService{

    @Value("${openai.model}")
    private String model;

    // 논문 가져오는 프롬포트
    @Value("${pubmed.prompt}")
    private String pubmedPrompt;

    // 논문 초록 요약 프롬포트
    @Value("${pubmed.info}")
    private String pubmedInfo;

    // OpenAI용 webClient
    private final WebClient openAiWebClient;

    // 논문 repository
    private final JournalRepository journalRepository;

    // 회원 repository
    private final UserRepository userRepository;

    // 논문 조회 repository
    private final JournalSearchRepository journalSearchRepository;

    // 논문 식별 서비스
    private final FlagService flagService;

    // 북마크 서비스
    private final BookmarkService bookmarkService;
    
    // 저널 북마크 이름
    private static String journalFlagName = "JOURNAL";

    public JournalServiceImpl(@Qualifier(value = "openAiWebClient") WebClient webClient,
                         JournalRepository journalRepository,
                         UserRepository userRepository,
                         JournalSearchRepository journalSearchRepository,
                              FlagService flagService,
                              BookmarkService bookmarkService) {
        this.openAiWebClient = webClient;
        this.journalRepository = journalRepository;
        this.userRepository = userRepository;
        this.journalSearchRepository = journalSearchRepository;
        this.flagService = flagService;
        this.bookmarkService = bookmarkService;
    }

    /**
     * 자연어로 논문 검색
     * @param naturalRequest
     * @return
     */
    public List<ResponsePubmedDTO> getPubmedKeywords(String naturalRequest){
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", pubmedPrompt),
                        Map.of("role", "user", "content", "들어온 자연어 요청: " + naturalRequest)
                )
        );

        try {

            Map<String, Object> response = openAiWebClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            // "choices" 가져오기
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> firstChoice = choices.get(0); // 첫 번째 choice 가져오기
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");

            log.info("choices {}", choices);
            log.info("message {}", message);


            return exchangeResponsePubmedDTO((String) message.get("content"));

        } catch (WebClientResponseException e){
            throw new CustomException(ErrorCode.INTERNAL_OPENAI_ERROR);
        }
    }

    /**
     * 논문 상세보기
     */
    @Transactional
    public ResponseAbstractDTO summarizeAbstractByPmid(String userId, String journalPmid, ResponsePubmedDTO requestDTO) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", pubmedInfo),
                        Map.of("role", "user", "content", "들어온 PMID: " + journalPmid)
                )
        );

        try {
            Map<String, Object> response = openAiWebClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            // orElseGet 사용해서 코드 간략화
            Journal savedJournal = journalRepository.findByJournalPmid(journalPmid)
                    .orElseGet(() -> journalRepository.save(requestDTO.toEntity()));

            // user 조회
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

            // 논문과 유저를 기반으로 조회이력 찾기 (있으면 updateDate 변경, 없으면 생성)
            journalSearchRepository.findByUserAndJournal(user, savedJournal)
                    .ifPresentOrElse(
                            journalSearchRepository::save,
                            () -> {
                                journalSearchRepository.save(new JournalSearch(savedJournal, user));
                            });
            
            // "choices" 가져오기
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> firstChoice = choices.get(0); // 첫 번째 choice 가져오기
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");

            log.info("choices {}", choices);
            log.info("message {}", message);

            // "content" 필드 반환 (초록 요약본)
            String journalAbstract = (String) message.get("content");

            // 반환하는 논문 초록 요약 담기
            return new ResponseAbstractDTO(journalPmid, requestDTO, journalAbstract);

        } catch (WebClientResponseException e){
            throw new CustomException(ErrorCode.INTERNAL_OPENAI_ERROR);
        }
    }

    /**
     * 조회 (조회순, 북마크순)
     */
    @Override
    public List<ResponseJournalSearchDTO> getJournalTop100(String sortBy) {

        Pageable top100 = PageRequest.of(0, 100); // 상위 100개
        if (sortBy.equals("select")){
            return journalSearchRepository.findTopJournalsWithSearchCount(top100).getContent();

        } else if (sortBy.equals("bookmark")){
            return null;
        } else {
            return null;
        }
    }

    /**
     * 나온 논문 데이터들 잘라서 쓰기 DTO로 변환
     */
    private List<ResponsePubmedDTO> exchangeResponsePubmedDTO(String content){
        // DTO를 담을 리스트
        List<ResponsePubmedDTO> pubmedDTOS = new ArrayList<>();

        // 논문 데이터들 \n\n으로 구분
        String[] journals = content.split("\n\n");
        log.info("journals {}", journals);
        // 각 논문들 파싱
        for (String journal : journals) {
            log.info("journal {}", journal);
            try {
                // 데이터를 개별 필드로 나눔
                String[] lines = journal.split("\n");

                log.info("lines {}", lines);
                log.info("lines.length {}", lines.length);

                String title = getField(lines, "Title:");
                String koreanTitle = getField(lines, "한글:");
                String source = getField(lines, "저널:");
                String pubDate = getField(lines, "발행일:");
                String size = getField(lines, "사이즈:");
                List<String> authors = Arrays.asList(getField(lines, "저자:").split(", "));
                String doi = getField(lines, "DOI:");
                String pmid = getField(lines, "PMID:");
                // DTO 생성 후 리스트에 추가
                pubmedDTOS.add(new ResponsePubmedDTO(title, koreanTitle, source, pubDate, size, authors, doi, pmid));
            } catch (Exception e) {
                System.err.println("Error journal: " + journal);
                e.printStackTrace();
            }
        }

        return pubmedDTOS;
    }

    /**
     * 논문 북마크
     */
    public boolean journalBookmark(String userId, Long journalSeq){

        // User 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 게시판 식별
        Flag flag = flagService.createFlag(journalFlagName, journalSeq);

        // true (북마크), false (북마크 해제)
        return bookmarkService.toggleBookmark(journalFlagName, journalSeq, userId);
    }

    /**
     * 내가 조회한 논문
     */
    public List<ResponseJournalLogDTO> getMySearchJournal(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return journalSearchRepository.findJournalLogs(user);
    }

    /**
     * 내가 북마크한 논문
     */
    public List<ResponseJournalLogDTO> getMyBookmarkJournal(String userId){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return bookmarkService.findByUserAndFlagType(user, journalFlagName)
                .stream()
                .map(bookmarkDTO -> {
                    try {
                        // Journal 조회
                        Journal journal = journalRepository.findById(bookmarkDTO.getFlag().getFlagEntitySeq())
                                .orElseThrow(() -> {
                                    log.warn("Flag 없음: {}", bookmarkDTO.getFlag().getFlagEntitySeq());
                                    return new CustomException(ErrorCode.NOT_FOUND_JOURNAL);
                                });

                        log.info("Flag 있음: {}", bookmarkDTO.getBookmarkSeq());

                        // ResponseJournalLogDTO 생성
                        return new ResponseJournalLogDTO(journal, bookmarkDTO.getCreateAt());
                    } catch (CustomException e) {
                        log.error("북마크 처리 실패 - bookmarkSeq: {}, 이유: {}", bookmarkDTO.getBookmarkSeq(), e.getMessage());
                        return null; // 실패한 항목은 null 반환
                    }
                })
                .filter(Objects::nonNull) // null인 항목 제외
                .toList();
    }

    /**
     * 필드 값 추출 메서드
     */
    private String getField(String[] lines, String key){
        for (String line : lines) {
            if (line.startsWith(key)) {
                log.info("line {}", line);
                return line.substring(line.indexOf(key) + key.length()).trim();
            }
        }
        return "";
    }


}
