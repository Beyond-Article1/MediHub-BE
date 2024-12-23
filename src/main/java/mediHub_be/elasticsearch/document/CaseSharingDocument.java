package mediHub_be.elasticsearch.document;

import lombok.*;
import mediHub_be.case_sharing.entity.CaseSharing;
import mediHub_be.config.elasticsearch.BaseSearchDocument;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 케이스 공유 인덱스 (DB table 개념)에 데이터 저장
@Document(indexName = "case_sharing")
//@Setting(settingPath = "/elasticsearch/settings/settings.json")
//@Mapping(mappingPath = "/elasticsearch/mappings/mappings.json")
public class CaseSharingDocument extends BaseSearchDocument {

    @Field(name = "case_sharing_title", type = FieldType.Text)
    private String caseSharingTitle;

    public static CaseSharingDocument from(CaseSharing caseSharing) {

        CaseSharingDocument caseSharingDocument = CaseSharingDocument.builder()
                .caseSharingTitle(caseSharing.getCaseSharingTitle())
                .build();

        caseSharingDocument.setId(caseSharing.getCaseSharingSeq());

        return caseSharingDocument;
    }
}