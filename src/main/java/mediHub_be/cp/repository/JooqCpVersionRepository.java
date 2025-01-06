package mediHub_be.cp.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ErrorCode;
import mediHub_be.cp.dto.ResponseCpDTO;
import org.jooq.*;
import org.jooq.generated.tables.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.View;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JooqCpVersionRepository {

    // jOOQ DSLContext 인스턴스
    private final DSLContext dslContext;

    public List<ResponseCpDTO> findCpVersionByCategory(List<Long> cpSearchCategoryDataArray) {
        log.info("DB 조회 시작");
        JCp CP = JCp.CP;
        JCpVersion CP_VERSION = JCpVersion.CP_VERSION;
        JCpSearchData CP_SEARCH_DATA = JCpSearchData.CP_SEARCH_DATA;
        JCpSearchCategoryData CP_SEARCH_CATEGORY_DATA = JCpSearchCategoryData.CP_SEARCH_CATEGORY_DATA;
        JUser USER = JUser.USER;
        JPart PART = JPart.PART;

        Condition condition = DSL.trueCondition();

        // cpSearchCategoryDataArray가 비어있지 않은 경우 IN 조건 추가
        if (!cpSearchCategoryDataArray.isEmpty()) {
            // IN 조건으로 변경
            condition = condition.and(CP_SEARCH_DATA.CP_SEARCH_CATEGORY_DATA_SEQ.in(cpSearchCategoryDataArray));
        }
        log.info("조건 추가 완료");

        // 서브쿼리: 각 CP의 최신 버전만 선택
        Table<Record3<Long, Long, LocalDateTime>> subQuery = dslContext
                .select(
                        CP_VERSION.CP_VERSION_SEQ,
                        CP_VERSION.CP_SEQ,
                        DSL.max(CP_VERSION.CREATED_AT).as("max_created_at")
                )
                .from(CP_VERSION)
                .groupBy(CP_VERSION.CP_SEQ) // CP_VERSION_SEQ와 CP_SEQ로 그룹화
                .asTable("latest_versions");

        // 메인 쿼리: 최신 버전만 선택
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
                .join(subQuery).on(CP_VERSION.CP_VERSION_SEQ.eq(subQuery.field("cp_version_seq", Long.class))) // 서브쿼리 조인
                .where(condition)
                .fetchInto(ResponseCpDTO.class);

        log.info("조회 성공");

        // DTO 리스트 로그 출력
        log.info("조회된 정보: {}", dtoList);
        return dtoList;
    }

    public ResponseCpDTO findCpVersionByCpVersionSeqAndCpVersion(long cpVersionSeq, String cpVersion) {
        log.info("DB 조회 시작");
        JCp CP = JCp.CP;
        JCpVersion CP_VERSION = JCpVersion.CP_VERSION;
        JUser USER = JUser.USER;
        JPart PART = JPart.PART;

        // 첫 번째 쿼리: cpVersionSeq로 cpName 조회
        String foundCpName = dslContext
                .select(CP.CP_NAME)
                .from(CP_VERSION)
                .join(CP).on(CP_VERSION.CP_SEQ.eq(CP.CP_SEQ))
                .where(CP_VERSION.CP_VERSION_SEQ.eq(cpVersionSeq))
                .fetchOne(CP.CP_NAME);

        if (foundCpName == null) {
            log.error(cpVersionSeq + "에 해당하는 CP를 찾지 못했습니다.");
            throw new CustomException(ErrorCode.NOT_FOUND_CP_VERSION);
        } else {
            log.info("찾은 이름: " + foundCpName);
        }

        // 두 번째 쿼리: cpName과 cpVersion으로 데이터 조회
        ResponseCpDTO dto = dslContext
                .select(
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
                .join(USER).on(CP_VERSION.USER_SEQ.eq(USER.USER_SEQ))
                .join(PART).on(USER.PART_SEQ.eq(PART.PART_SEQ))
                .where(CP.CP_NAME.eq(foundCpName).and(CP_VERSION.CP_VERSION_.eq(cpVersion)))
                .fetchOneInto(ResponseCpDTO.class);

        log.info("조회 성공");

        // DTO 로그 출력
//        log.info("조회된 데이터: {}", dto);

        return dto;
    }
}
