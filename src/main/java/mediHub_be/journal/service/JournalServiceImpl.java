package mediHub_be.journal.service;

import lombok.extern.slf4j.Slf4j;
import mediHub_be.board.service.BookmarkService;
import mediHub_be.board.service.FlagService;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.common.response.JournalResponse;
import mediHub_be.journal.dto.ResponseAbstractDTO;
import mediHub_be.journal.dto.ResponseJournalLogDTO;
import mediHub_be.journal.dto.ResponseJournalRankDTO;
import mediHub_be.journal.dto.ResponsePubmedDTO;
import mediHub_be.journal.entity.Journal;
import mediHub_be.journal.entity.JournalSearch;
import mediHub_be.journal.repository.JournalRepository;
import mediHub_be.journal.repository.JournalSearchRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.service.UserService;
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

    // 회원 서비스
    private final UserService userService;

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
                         UserService userService,
                         JournalSearchRepository journalSearchRepository,
                              FlagService flagService,
                              BookmarkService bookmarkService) {
        this.openAiWebClient = webClient;
        this.journalRepository = journalRepository;
        this.userService = userService;
        this.journalSearchRepository = journalSearchRepository;
        this.flagService = flagService;
        this.bookmarkService = bookmarkService;
    }

    /**
     * 자연어로 논문 검색
     * @param naturalRequest
     * @return
     */
    public JournalResponse<?> getPubmedKeywords(String naturalRequest){

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

            List<ResponsePubmedDTO> content =
                    exchangeResponsePubmedDTO((String) message.get("content"));

            if (content.isEmpty()){
                return new JournalResponse<>((String) message.get("content"));
            } else{
                return new JournalResponse<>(content);
            }

        } catch (WebClientResponseException e) {
            int statusCode = e.getStatusCode().value();
            String errorBody = e.getResponseBodyAsString();

            log.error("OpenAI API Error - Status: {}, Body: {}", statusCode, errorBody);

            // 더 자세하게 예외 처리하기
            if (statusCode == 401) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_OPENAI_API);
            } else if (statusCode == 429) {
                throw new CustomException(ErrorCode.RATE_LIMIT_EXCEEDED);
            } else {
                throw new CustomException(ErrorCode.INTERNAL_OPENAI_ERROR);
            }
        }
    }

    /**
     * 논문 상세보기
     */
    @Transactional
    public ResponseAbstractDTO summarizeAbstractByPmid(Long userSeq, String journalPmid, ResponsePubmedDTO requestDTO) {
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

            log.info("response {}", response);

            // orElseGet 사용해서 코드 간략화
            Journal savedJournal = journalRepository.findByJournalPmid(journalPmid)
                    .orElseGet(() -> journalRepository.save(requestDTO.toEntity()));

            // user 조회
            User user = userService.findUser(userSeq);

            // 논문과 유저를 기반으로 조회이력 찾기 (있으면 updateDate 변경, 없으면 생성)
            journalSearchRepository.findByUserAndJournal(user, savedJournal)
                    .ifPresentOrElse(
                            journalSearchRepository::save,
                            () -> {
                                journalSearchRepository.save(new JournalSearch(savedJournal, user));
                            });

            // journal flag 생성
            flagService.createFlag(journalFlagName, savedJournal.getJournalSeq());
            
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
    public List<ResponseJournalRankDTO> getJournalTop100(String sortBy, Long userSeq) {

        Pageable top100 = PageRequest.of(0, 100); // 상위 100개

        List<ResponseJournalRankDTO> content = new ArrayList<>();

        if (sortBy.equals("select")) {       // 조회순
            content = journalSearchRepository.findTopJournalsWithSearchCount(top100).getContent();

        } else if (sortBy.equals("bookmark")) {  // 북마크순
            content = journalSearchRepository.findTopJournalsWithBookmarkCount(top100).getContent();
        }

        if (userSeq != null && !content.isEmpty()) {
            User user = userService.findUser(userSeq);
            content.forEach(journal -> journal.isBookmark(bookmarkService.isBookmarked(journalFlagName, journal.getJournalSeq(), user.getUserId())));
        }
        return content;
    }

    /**
     * 나온 논문 데이터들 잘라서 쓰기 DTO로 변환
     */
    private List<ResponsePubmedDTO> exchangeResponsePubmedDTO(String content){
        // DTO를 담을 리스트
        List<ResponsePubmedDTO> pubmedDTOS = new ArrayList<>();

        // 논문 데이터들 \n\n으로 구분
        String[] journals = content.split("\n\n");
        if (!journals[0].startsWith("해당하는 논문들"))
            return pubmedDTOS;

        // 각 논문들 파싱
        for (String journal : journals) {
            log.info("journal {}", journal);
            try {
                // 데이터를 개별 필드로 나눔
                String[] lines = journal.split("\n");

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
    public boolean journalBookmark(Long userSeq, Long journalSeq){

        // User 확인
        User user = userService.findUser(userSeq);

        // 게시판 식별 (있으면 조회 없으면 생성)
        flagService.createFlag(journalFlagName, journalSeq);

        // true (북마크), false (북마크 해제)
        return bookmarkService.toggleBookmark(journalFlagName, journalSeq, user.getUserId());
    }

    /**
     * 내가 조회한 논문
     */
    public List<ResponseJournalLogDTO> getMySearchJournal(Long userSeq) {
        User user = userService.findUser(userSeq);

        return journalSearchRepository.findJournalLogs(user);
    }

    /**
     * 내가 북마크한 논문
     */
    public List<ResponseJournalLogDTO> getMyBookmarkJournal(Long userSeq){
        User user = userService.findUser(userSeq);

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
