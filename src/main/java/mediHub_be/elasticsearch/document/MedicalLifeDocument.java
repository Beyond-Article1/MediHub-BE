package mediHub_be.elasticsearch.document;

import lombok.*;
import mediHub_be.config.elasticsearch.BaseSearchDocument;
import mediHub_be.medicalLife.entity.MedicalLife;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 메디컬 라이프 인덱스 (DB table 개념)에 데이터 저장
@Document(indexName = "medical_life")
//@Setting(settingPath = "/elasticsearch/settings/settings.json")
//@Mapping(mappingPath = "/elasticsearch/mappings/mappings.json")
public class MedicalLifeDocument extends BaseSearchDocument {

    @Field(name = "medical_life_title", type = FieldType.Text)
    private String medicalLifeTitle;

    public static MedicalLifeDocument from(MedicalLife medicalLife) {

        MedicalLifeDocument medicalLifeDocument = MedicalLifeDocument.builder()
                .medicalLifeTitle(medicalLife.getMedicalLifeTitle())
                .build();

        medicalLifeDocument.setId(medicalLife.getMedicalLifeSeq());

        return medicalLifeDocument;
    }
}