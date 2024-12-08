package mediHub_be.openai.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OpenAiService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.key}")
    private String openAiKey;

    @Value("${openai.api.url}")
    private String apiURL;

    // OpenAI용 webClient
    private final WebClient openAiWebClient;

    // 일반 webClient
    private final WebClient apiWebClient;

    public OpenAiService(@Qualifier(value = "openAiWebClient") WebClient webClient) {
        this.openAiWebClient = webClient;
        this.apiWebClient = WebClient.builder()
                .baseUrl("https://eutils.ncbi.nlm.nih.gov/entrez/eutils")
                .build();
    }

    /**
     * 자연어에서의 키워드 추출 모델
     * @param naturalRequest
     * @return
     */
    public String changePubmedKeywords(String naturalRequest){
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "너는 자연어를 받아서 Pubmed API로 요청할 수 있게 키워드들을 뽑아주는 Pubmed용 자연어 처리 모델이야. " +
                                        "그리고 키워드들을 추출한 뒤, 그 키워드들의 중요도와 결합 방식을 `(AND)`, `(OR)`, `(NOT)`을 통해 명시해줘. " +
                                        "예시: '톱에 의해 손가락이 절단되었을 때에 관련된 논문을 찾아줘' -> 키워드: '톱, 손가락, 절단' " +
                                        "그리고 중요도와 결합 방식은 '톱 (AND), 손가락 (AND), 절단 (AND)' 이런 형식으로 반환해줘. " +
                                        "또한, 결과는 아래와 같은 형식으로 반환되어야 해:\n" +
                                        "\"키워드 추출 결과:\nADHD, 발생, 이유, 나이\n\n중요도 및 결합 방식:\nADHD (AND), 발생 (AND), 이유 (AND), 나이 (NOT)\" " +
                                        "이 형식에 맞춰서 반환해줘."),
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

            // "content" 필드 반환
            String pubmedKeyword = keywordsChangeForPubmed((String) message.get("content"));

            log.info("pubmed keyword {}", pubmedKeyword);

            List<String> strings = searchPubmed(pubmedKeyword, 20);

            log.info("strings {}", strings);

            return strings.get(0);

        } catch (WebClientResponseException e){
            throw new RuntimeException("OpenAI API 호출 오류: " + e.getMessage());
        }
    }

    /**
     * OpenAI의 반환값을 PubMed API에 맞는 형식으로 변환
     * @param extractedKeywords OpenAI에서 반환된 키워드 및 중요도 텍스트
     * @return PubMed API의 term 형식
     */
    public String keywordsChangeForPubmed(String extractedKeywords) {
        // 키워드 추출 결과와 중요도 정보를 파싱하는 정규식
        Pattern keywordPattern = Pattern.compile("([\\w가-힣]+) \\((AND|OR|NOT)\\)");
        Matcher matcher = keywordPattern.matcher(extractedKeywords);

        // 각 키워드를 PubMed 형식으로 조합
        String pubmedQuery = matcher.results()
                .map(result -> {
                    String keyword = result.group(1); // 키워드
                    String operator = result.group(2); // 중요도 (AND, OR, NOT)

                    // PubMed 형식으로 변환
                    return operator.equals("NOT")
                            ? String.format("NOT %s", keyword)
                            : keyword;
                })
                .collect(Collectors.joining(" "));

        return pubmedQuery;
    }

    /**
     * 키워드들을 조합해서 pubmed 검색어로 바꿔주는 메서드
     * @param keywords
     * @return ex. 독에 의한 ADHD에 관한 논문을 찾아줘 개는 빼고 -> ADHD AND POISON NOT DOG
     */
    public String buildPubmed(Map<String, String> keywords){
        StringBuilder pubmedQuery = new StringBuilder();

        for (Map.Entry<String, String> entry : keywords.entrySet()) {
            String keyword = entry.getKey();
            String operator = entry.getValue();     // AND, OR, NOT

            if (!pubmedQuery.isEmpty()) {
                pubmedQuery.append(" ").append(operator).append(" ");
            }
            pubmedQuery.append(keyword);
        }

        // 생성된 검색문 (ex. 식중독 AND 발생 AND 원인)
        return pubmedQuery.toString();
    }

    /**
     * pubmed 검색어로 PMID를 검색하는 pubmed api 호출 메서드 (esearch)
     * @param query
     * @param maxResults
     * @return
     */
    public List<String> searchPubmed(String query, int maxResults){
        String response = apiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/esearch.fcgi")
                        .queryParam("db", "pubmed")
                        .queryParam("term", query)
                        .queryParam("retmax", maxResults)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return parsePubmedIdsFromXml(response);
    }

    /**
     * response의 예시 XML
     * <eSearchResult>
     *   <Count>100</Count>
     *   <RetMax>5</RetMax>
     *   <RetStart>0</RetStart>
     *   <IdList>
     *     <Id>37165056</Id>
     *     <Id>37165055</Id>
     *     <Id>37165054</Id>
     *     <Id>37165053</Id>
     *     <Id>37165052</Id>
     *   </IdList>
     * </eSearchResult>
     *
     * 여기서 Id들만 뽑아온다
     * @param xmlResponse
     * @return Id 값들이 들어있는 List
     */
    private List<String> parsePubmedIdsFromXml(String xmlResponse) {
        // XML 파싱하여 PMID 목록 반환
        List<String> pubMedIds = new ArrayList<>();

        // Jsoup을 사용하여 XML 파싱
        Document doc = Jsoup.parse(xmlResponse, "", org.jsoup.parser.Parser.xmlParser());

        // <IdList> 태그 내의 모든 <Id> 태그 찾기.
        Elements idElements = doc.select("IdList Id");

        for (Element idElement : idElements) {
            pubMedIds.add(idElement.text()); // PubMed ID 추출
        }

        log.info("pubMedIds {}", pubMedIds);

        return pubMedIds;
    }

    // PMID로 논문들 가져오기
    public List<Map<String, String>> fetchPubmedDetails(List<String> pmids) {
        String response = apiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/esummary.fcgi")
                        .queryParam("db", "pubmed")
                        .queryParam("id", String.join(",", pmids))
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return parsePubmedDetailsFromXml(response);
    }

    private List<Map<String, String>> parsePubmedDetailsFromXml(String xmlResponse) {
        // XML 파싱하여 논문 제목, 저자 등 반환
        return List.of(
                Map.of("title", "Novel cancer therapy approaches", "source", "Nature"),
                Map.of("title", "Advancements in cancer immunotherapy", "source", "Science")
        );
    }



}
