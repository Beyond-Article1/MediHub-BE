package mediHub_be.elasticsearch.document;

import lombok.*;
import mediHub_be.anonymousBoard.entity.AnonymousBoard;
import mediHub_be.board.entity.Keyword;
import mediHub_be.config.elasticsearch.BaseSearchDocument;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 익명 게시판 인덱스 (DB table 개념)에 데이터 저장
@Document(indexName = "anonymous_board")
public class AnonymousBoardDocument extends BaseSearchDocument {

    @Field(name = "anonymous_board_seq", type = FieldType.Text)
    private String anonymousBoardSeq;

    @Field(name = "anonymous_board_title", type = FieldType.Text, analyzer = "korean")
    private String anonymousBoardTitle;

    @Field(name = "anonymous_board_content", type = FieldType.Text, analyzer = "korean")
    private String anonymousBoardContent;

    @Field(name = "anonymous_board_view_count", type = FieldType.Text)
    private String anonymousBoardViewCount;

    @Field(name = "created_at", type = FieldType.Text)
    private String createdAt;

    @Field(name = "keyword_list", type = FieldType.Text, analyzer = "korean")
    private List<String> keywordList;

    public static AnonymousBoardDocument from(AnonymousBoard anonymousBoard, List<Keyword> keywordList) {

        AnonymousBoardDocument anonymousBoardDocument = AnonymousBoardDocument.builder()
                .anonymousBoardSeq(String.valueOf(anonymousBoard.getAnonymousBoardSeq()))
                .anonymousBoardTitle(anonymousBoard.getAnonymousBoardTitle())
                .anonymousBoardContent(anonymousBoard.getAnonymousBoardContent())
                .anonymousBoardViewCount(String.valueOf(anonymousBoard.getAnonymousBoardViewCount()))
                .createdAt(String.valueOf(anonymousBoard.getCreatedAt()))
                .keywordList(keywordList.stream()
                        .map(Keyword::getKeywordName)
                        .collect(Collectors.toList()))
                .build();

        anonymousBoardDocument.setId(anonymousBoard.getAnonymousBoardSeq());

        return anonymousBoardDocument;
    }
}