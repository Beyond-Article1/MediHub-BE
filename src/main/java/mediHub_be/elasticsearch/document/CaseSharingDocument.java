package mediHub_be.elasticsearch.document;

import lombok.*;
import mediHub_be.board.entity.Keyword;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.config.elasticsearch.BaseSearchDocument;
import mediHub_be.user.entity.User;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 케이스 공유 인덱스 (DB table 개념)에 데이터 저장
@Document(indexName = "case_sharing")
public class CaseSharingDocument extends BaseSearchDocument {

    @Field(name = "case_sharing_seq", type = FieldType.Text)
    private String caseSharingSeq;

    @Field(name = "case_sharing_title", type = FieldType.Text, analyzer = "korean")
    private String caseSharingTitle;

    @Field(name = "case_sharing_content", type = FieldType.Text, analyzer = "korean")
    private String caseSharingContent;

    @Field(name = "case_author", type = FieldType.Text)
    private String caseAuthor;

    @Field(name = "case_author_part_name", type = FieldType.Text)
    private String caseAuthorPartName;

    @Field(name = "case_author_rank_name", type = FieldType.Text)
    private String caseAuthorRankName;

    @Field(name = "case_sharing_view_count", type = FieldType.Text)
    private String caseSharingViewCount;

    @Field(name = "reg_date", type = FieldType.Text)
    private String regDate;

    @Field(name = "keyword_list", type = FieldType.Text, analyzer = "korean")
    private List<String> keywordList;

    public static CaseSharingDocument from(CaseSharing caseSharing, List<Keyword> keywordList) {

        User user = caseSharing.getUser();
        CaseSharingDocument caseSharingDocument = CaseSharingDocument.builder()
                .caseSharingSeq(String.valueOf(caseSharing.getCaseSharingSeq()))
                .caseSharingTitle(caseSharing.getCaseSharingTitle())
                .caseSharingContent(caseSharing.getCaseSharingContent())
                .caseAuthor(user.getUserName())
                .caseAuthorPartName(user.getPart().getPartName())
                .caseAuthorRankName(user.getRanking().getRankingName())
                .caseSharingViewCount(String.valueOf(caseSharing.getCaseSharingViewCount()))
                .regDate(String.valueOf(caseSharing.getCreatedAt()))
                .keywordList(keywordList.stream()
                        .map(Keyword::getKeywordName)
                        .collect(Collectors.toList()))
                .build();

        caseSharingDocument.setId(caseSharing.getCaseSharingSeq());

        return caseSharingDocument;
    }
}