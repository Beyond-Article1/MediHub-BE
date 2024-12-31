package mediHub_be.elasticsearch.document;

import lombok.*;
import mediHub_be.config.elasticsearch.BaseSearchDocument;
import mediHub_be.cp.dto.ResponseCpDTO;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// CP 인덱스 (DB table 개념)에 데이터 저장
@Document(indexName = "cp")
public class CpDocument extends BaseSearchDocument {

    @Field(name = "cp_name", type = FieldType.Text, analyzer = "korean")
    private String cpName;

    @Field(name = "cp_description", type = FieldType.Text, analyzer = "korean")
    private String cpDescription;

    @Field(name = "cp_view_count", type = FieldType.Text)
    private String cpViewCount;

    @Field(name = "cp_version_seq", type = FieldType.Text)
    private String cpVersionSeq;

    @Field(name = "cp_url", type = FieldType.Text)
    private String cpUrl;

    @Field(name = "created_at", type = FieldType.Text)
    private String createdAt;

    @Field(name = "is_bookmarked", type = FieldType.Text)
    private String isBookmarked;

    public static CpDocument from(ResponseCpDTO responseCpDTO) {

        CpDocument cpDocument = CpDocument.builder()
                .cpName(responseCpDTO.getCpName())
                .cpDescription(responseCpDTO.getCpDescription())
                .cpViewCount(String.valueOf(responseCpDTO.getCpViewCount()))
                .cpVersionSeq(String.valueOf(responseCpDTO.getCpVersionSeq()))
                .cpUrl(responseCpDTO.getCpUrl())
                .createdAt(String.valueOf(responseCpDTO.getCreatedAt()))
                .isBookmarked(String.valueOf(responseCpDTO.isBookmarked()))
                .build();

        cpDocument.setId(responseCpDTO.getCpVersionSeq());

        return cpDocument;
    }
}