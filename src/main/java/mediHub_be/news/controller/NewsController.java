package mediHub_be.news.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import mediHub_be.news.dto.NewsApiResponse;
import mediHub_be.news.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@Tag(name = "의료 뉴스", description = "의료 뉴스 API")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/top3")
    public ResponseEntity<ApiResponse<List<NewsApiResponse.Article>>> getTop3NewsArticles() {
        List<NewsApiResponse.Article> articles = newsService.getTop3NewsArticles();
        return ResponseEntity.ok(ApiResponse.ok(articles));
    }

}
