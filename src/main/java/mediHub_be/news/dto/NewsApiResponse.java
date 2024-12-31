package mediHub_be.news.dto;

import lombok.Data;

import java.util.List;

@Data
public class NewsApiResponse {
    private String status;          // 응답 상태 ('ok' 또는 'error')
    private int totalResults;       // 총 결과 수
    private List<Article> articles; // 기사 리스트

    @Data
    public static class Article {
        private String author;      // 작성자
        private String title;       // 기사 제목
        private String description; // 기사 설명
        private String urlToImage;  // 이미지 url
        private String url;         // 기사 url
        private String publishedAt; // 게시 날짜
    }
}
