package mediHub_be.cp.repository;

import lombok.RequiredArgsConstructor;
import mediHub_be.cp.dto.ResponseCpDTO;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.generated.tables.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JooqCpVersionRepository {

    // jOOQ DSLContext 인스턴스
    private final DSLContext dslContext;

    // jOOQ에서 생성된 테이블 클래스 인스턴스
    private final JCpVersion CP_VERSION = JCpVersion.CP_VERSION;


    public List<ResponseCpDTO> findCpVersionByCategory(List<Long> cpSearchCategoryDataArray) {

        JCp CP = JCp.CP;
        JCpSearchData CP_SEARCH_DATA = JCpSearchData.CP_SEARCH_DATA;
        JCpSearchCategoryData CP_SEARCH_CATEGORY_DATA = JCpSearchCategoryData.CP_SEARCH_CATEGORY_DATA;
        JUser USER = JUser.USER;
        JPart PART = JPart.PART;

        // 조건을 동적으로 생성합니다.
        Condition condition = DSL.trueCondition(); // 기본 조건을 true로 설정

        // cpSearchCategoryDataArray의 모든 값을 AND 조건으로 추가
        for (Long dataSeq : cpSearchCategoryDataArray) {
            condition = condition.and(CP_SEARCH_CATEGORY_DATA.CP_SEARCH_CATEGORY_DATA_SEQ.eq(dataSeq));
        }

        return dslContext
                .selectDistinct(
                        CP_VERSION.CP_VERSION_SEQ.as("cpVersionSeq"),
                        CP.CP_NAME.as("cpName"),
                        CP.CP_DESCRIPTION.as("cpDescription"),
                        CP.CP_VIEW_COUNT.as("cpViewCount"),
                        CP_VERSION.CP_VERSION.as("cpVersion"),
                        CP_VERSION.CP_VERSION_DESCRIPTION.as("cpVersionDescription"),
                        CP_VERSION.CREATED_AT.as("createdAt"),
                        CP_VERSION.CP_URL.as("cpUrl"),
                        USER.USER_NAME.as("userName"),
                        USER.USER_ID.as("userId"),
                        PART.PART_NAME.as("partName"))
                .from(CP_VERSION)
                .join(CP).on(CP_VERSION.CP_SEQ.eq(CP.CP_SEQ))
                .join(CP_SEARCH_DATA).on(CP_VERSION.CP_VERSION_SEQ.eq(CP_SEARCH_DATA.CP_VERSION_SEQ))
                .join(CP_SEARCH_CATEGORY_DATA).on(CP_SEARCH_CATEGORY_DATA.CP_SEARCH_CATEGORY_DATA_SEQ.eq(CP_SEARCH_DATA.CP_SEARCH_CATEGORY_DATA_SEQ))
                .join(USER).on(CP_VERSION.USER_SEQ.eq(USER.USER_SEQ))
                .join(PART).on(USER.PART_SEQ.eq(PART.PART_SEQ))
                .where(condition) // 동적으로 생성된 조건을 사용
                .groupBy(
                        CP_VERSION.CP_VERSION_SEQ,
                        CP.CP_NAME,
                        CP.CP_DESCRIPTION,
                        CP.CP_VIEW_COUNT,
                        CP_VERSION.CP_VERSION,
                        CP_VERSION.CP_VERSION_DESCRIPTION,
                        CP_VERSION.CREATED_AT,
                        CP_VERSION.CP_URL,
                        USER.USER_NAME,
                        USER.USER_ID,
                        PART.PART_NAME
                )
                .fetchInto(ResponseCpDTO.class);
    }
}
