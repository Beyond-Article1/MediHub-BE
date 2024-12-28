package mediHub_be.cp.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.cp.dto.ResponseCpDTO;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.generated.tables.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JooqCpVersionRepository {

    // jOOQ DSLContext 인스턴스
    private final DSLContext dslContext;

    public List<ResponseCpDTO> findCpVersionByCategory(List<Long> cpSearchCategoryDataArray) {
        log.info("조회 시작");
        JCp CP = JCp.CP;
        JCpVersion CP_VERSION = JCpVersion.CP_VERSION;
        JCpSearchData CP_SEARCH_DATA = JCpSearchData.CP_SEARCH_DATA;
        JCpSearchCategoryData CP_SEARCH_CATEGORY_DATA = JCpSearchCategoryData.CP_SEARCH_CATEGORY_DATA;
        JUser USER = JUser.USER;
        JPart PART = JPart.PART;

        Condition condition = DSL.trueCondition();

        if (!cpSearchCategoryDataArray.isEmpty()) {
            for (Long dataSeq : cpSearchCategoryDataArray) {
                condition = condition.and(CP_SEARCH_DATA.CP_SEARCH_CATEGORY_DATA_SEQ.eq(dataSeq));
            }
//            log.info("추가된 조건: {}", condition);
        }
        log.info("조건 추가 완료");

        // 쿼리 실행 후 Record 리스트를 가져오기
        List<ResponseCpDTO> dtoList = dslContext
                .selectDistinct(
                        CP_VERSION.CP_VERSION_SEQ.as("cpVersionSeq"),
                        CP.CP_NAME.as("cpName"),
                        CP.CP_DESCRIPTION.as("cpDescription"),
                        CP.CP_VIEW_COUNT.as("cpViewCount"),
                        CP_VERSION.CP_VERSION_.as("cpVersion"),
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
                .where(condition)
                .fetchInto(ResponseCpDTO.class);
//                .map(record -> ResponseCpDTO.builder()
//                        .cpVersionSeq(record.getValue(CP_VERSION.CP_VERSION_SEQ))
//                        .cpName(record.getValue(CP.CP_NAME))
//                        .cpDescription(record.getValue(CP.CP_DESCRIPTION))
//                        .cpViewCount(record.getValue(CP.CP_VIEW_COUNT))
//                        .cpVersion(record.getValue(CP_VERSION.CP_VERSION_))
//                        .cpVersionDescription(record.getValue(CP_VERSION.CP_VERSION_DESCRIPTION))
//                        .createdAt(record.getValue(CP_VERSION.CREATED_AT))
//                        .cpUrl(record.getValue(CP_VERSION.CP_URL))
//                        .userName(record.getValue(USER.USER_NAME))
//                        .userId(record.getValue(USER.USER_ID))
//                        .partName(record.getValue(PART.PART_NAME))
//                        .build());

        log.info("조회 성공");

        // DTO 리스트 로그 출력
        log.info("조회된 정보: {}", dtoList);
        return dtoList;
    }
}
