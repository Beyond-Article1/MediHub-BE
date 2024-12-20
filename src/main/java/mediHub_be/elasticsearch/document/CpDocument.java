package mediHub_be.elasticsearch.document;

import lombok.*;
import mediHub_be.config.elasticsearch.BaseSearchDocument;
import mediHub_be.cp.entity.Cp;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// CP 인덱스 (DB table 개념)에 데이터 저장
@Document(indexName = "cp")
//@Setting(settingPath = "/elasticsearch/settings/settings.json")
//@Mapping(mappingPath = "/elasticsearch/mappings/mappings.json")
public class CpDocument extends BaseSearchDocument {

    @Field(name = "cp_name", type = FieldType.Text)
    private String cpName;

    public static CpDocument from(Cp cp) {

        CpDocument cpDocument = CpDocument.builder()
                .cpName(cp.getCpName())
                .build();

        cpDocument.setId(cp.getCpSeq());

        return cpDocument;
    }
}