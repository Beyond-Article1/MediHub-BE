package mediHub_be.elasticsearch.document;


import lombok.*;
import mediHub_be.config.elasticsearch.BaseSearchDocument;
import mediHub_be.user.entity.User;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 직원 인덱스 (DB table 개념)에 데이터 저장
@Document(indexName = "user")
//@Setting(settingPath = "/elasticsearch/settings/settings.json")
//@Mapping(mappingPath = "/elasticsearch/mappings/mappings.json")
public class UserDocument extends BaseSearchDocument {

    @Field(name = "user_name", type = FieldType.Text)
    private String userName;

    public static UserDocument from(User user) {

        UserDocument userDocument = UserDocument.builder()
                .userName(user.getUserName())
                .build();

        userDocument.setId(user.getUserSeq());

        return userDocument;
    }
}