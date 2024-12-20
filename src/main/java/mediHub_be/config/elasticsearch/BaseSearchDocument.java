package mediHub_be.config.elasticsearch;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseSearchDocument {

    // 검색 문서 기본 틀
    @Id
    protected Long id;
}