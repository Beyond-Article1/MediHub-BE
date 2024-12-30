package mediHub_be.news.service;

import lombok.extern.slf4j.Slf4j;
import mediHub_be.news.dto.NewsApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class NewsService {

    private final WebClient webClient;

    public NewsService(WebClient newsApiWebClient) {
        this.webClient = newsApiWebClient;
    }

    public List<NewsApiResponse.Article> getTop3NewsArticles() {
        log.info("NewsApi 호출중 ...");

        // 오늘 날짜와 일주일 전 날짜 계산
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime oneWeekAgo = currentDate.minusDays(7);

        // ISO 8601 형식으로 변환
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String currentDateIso = currentDate.format(isoFormatter);
        String oneWeekAgoIso = oneWeekAgo.format(isoFormatter);

        // API 호출 및 응답 처리
        NewsApiResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/everything")
                        .queryParam("q", "medicine innovation")
                        .queryParam("from", oneWeekAgoIso)
                        .queryParam("to", currentDateIso)
                        .queryParam("pageSize", 3)
                        .build())
                .retrieve()
                .bodyToMono(NewsApiResponse.class)
                .block();

        log.info("NewsApi 호출 결과 response : {}", response);
        return response != null ? response.getArticles() : null;
    }
}
