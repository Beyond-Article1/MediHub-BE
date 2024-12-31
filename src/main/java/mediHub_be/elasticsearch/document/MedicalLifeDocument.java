package mediHub_be.elasticsearch.document;

import lombok.*;
import mediHub_be.board.entity.Keyword;
import mediHub_be.config.elasticsearch.BaseSearchDocument;
import mediHub_be.medicalLife.entity.MedicalLife;
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
// 메디컬 라이프 인덱스 (DB table 개념)에 데이터 저장
@Document(indexName = "medical_life")
public class MedicalLifeDocument extends BaseSearchDocument {

    @Field(name = "medical_life_seq", type = FieldType.Text)
    private String medicalLifeSeq;

    @Field(name = "user_seq", type = FieldType.Text)
    private String userSeq;

    @Field(name = "user_name", type = FieldType.Text)
    private String userName;

    @Field(name = "part_seq", type = FieldType.Text)
    private String partSeq;

    @Field(name = "part_name", type = FieldType.Text)
    private String partName;

    @Field(name = "dept_seq", type = FieldType.Text)
    private String deptSeq;

    @Field(name = "dept_name", type = FieldType.Text)
    private String deptName;

    @Field(name = "ranking_name", type = FieldType.Text)
    private String rankingName;

    @Field(name = "medical_life_title", type = FieldType.Text, analyzer = "korean")
    private String medicalLifeTitle;

    @Field(name = "medical_life_content", type = FieldType.Text, analyzer = "korean")
    private String medicalLifeContent;

    @Field(name = "medical_life_view_count", type = FieldType.Text)
    private String medicalLifeViewCount;

    @Field(name = "created_at", type = FieldType.Text)
    private String createdAt;

    @Field(name = "keyword_list", type = FieldType.Text, analyzer = "korean")
    private List<String> keywordList;

    public static MedicalLifeDocument from(MedicalLife medicalLife, List<Keyword> keywordList) {

        User user = medicalLife.getUser();
        MedicalLifeDocument medicalLifeDocument = MedicalLifeDocument.builder()
                .medicalLifeSeq(String.valueOf(medicalLife.getMedicalLifeSeq()))
                .userSeq(String.valueOf(user.getUserSeq()))
                .userName(user.getUserName())
                .partSeq(String.valueOf(user.getPart().getPartSeq()))
                .partName(user.getPart().getPartName())
                .deptSeq(String.valueOf(user.getPart().getDept().getDeptSeq()))
                .deptName(user.getPart().getDept().getDeptName())
                .rankingName(user.getRanking().getRankingName())
                .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                .medicalLifeContent(medicalLife.getMedicalLifeContent())
                .medicalLifeViewCount(String.valueOf(medicalLife.getMedicalLifeViewCount()))
                .createdAt(String.valueOf(medicalLife.getCreatedAt()))
                .keywordList(keywordList.stream()
                        .map(Keyword::getKeywordName)
                        .collect(Collectors.toList()))
                .build();

        medicalLifeDocument.setId(medicalLife.getMedicalLifeSeq());

        return medicalLifeDocument;
    }
}