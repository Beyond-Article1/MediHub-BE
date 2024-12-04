package mediHub_be.case_sharing.entity;

import lombok.Getter;

@Getter
public enum OpenScope {
    PRIVATE("나만보기"),
    CLASS_OPEN("같은과만공개"),
    PUBLIC("전체공개");

    private final String openScope;

    OpenScope(String openScope) {this.openScope = openScope;}
}
