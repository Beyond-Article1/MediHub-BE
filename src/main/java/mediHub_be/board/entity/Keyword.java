package mediHub_be.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "keyword")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordSeq;

    private Long flagSeq; // 게시판 식별 플래그
    private String keywordName; // 키워드 명
    private Long keywordPostSeq; // 관련 게시물 seq
}