package mediHub_be.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * == 400  BAD_REQUEST ==
     */
    // 사용자 (User)
    NEED_LOGIN(40000, HttpStatus.BAD_REQUEST, "로그인이 필요한 서비스입니다."),

    INVALID_SELECT_SEQ(40001, HttpStatus.BAD_REQUEST, "유효하지 않은 선택 번호입니다."),

    // 시간관련
    NEED_AFTER_TIME(40002, HttpStatus.BAD_REQUEST, "현재 시간 이후로만 설정이 가능합니다."),

    // 필력 필드 관련
    REQUIRED_FIELD_MISSING(40003, HttpStatus.BAD_REQUEST, "필수 입력값이 누락되었습니다. 모든 필드를 확인해 주세요."),

    // 삭제 불가 데이터 요청
    CANNOT_DELETE_DATA(40903,HttpStatus.CONFLICT, "데이터 제약 조건으로 인해 삭제가 불가능합니다."),
    /**
     * == 401 UNAUTHORIZED ==
     */
    // 사용자 (user)
    USER_STATUS_BANED(40100, HttpStatus.UNAUTHORIZED, "정지된 회원입니다."),

    // 유효하지 않은 토큰 (Token)
    INVALID_TOKEN(40101, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    /**
     * == 403 FORBIDDEN ==
     */
    // 사용자 (user)
    UNAUTHORIZED_USER(40300, HttpStatus.FORBIDDEN, "해당하는 권한이 없습니다."),

    /**
     * == 404 NOT_FOUND ==
     */
    // 사용자 (user)
    NOT_FOUND_USER(40400, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_END_POINT(40401, HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),

    NOT_FOUND_CP(40402, HttpStatus.NOT_FOUND, "CP 버전을 찾을 수 없습니다."),
    NOT_FOUND_CP_VERSION(40402, HttpStatus.NOT_FOUND, "CP 버전을 찾을 수 없습니다."),
    NOT_FOUND_CP_OPINION(40403, HttpStatus.NOT_FOUND, "CP 의견을 찾을 수 없습니다."),
    NOT_FOUND_CP_OPINION_LOCATION(40404, HttpStatus.NOT_FOUND, "CP 의견 위치를 찾을 수 없습니다."),
    NOT_FOUND_CP_SEARCH_CATEGORY(40405, HttpStatus.NOT_FOUND, "CP 의견 위치를 찾을 수 없습니다."),
    NOT_FOUND_CP_SEARCH_CATEGORY_DATA(40406, HttpStatus.NOT_FOUND, "CP 의견 위치를 찾을 수 없습니다."),

    // 알림 (notify)
    NOT_FOUND_NOTIFY(40404, HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다."),

    // 채팅 (chat)
    NOT_FOUND_CHATROOM(40405, HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),

    // 익명 게시판 (anonymous_board)
    NOT_FOUND_ANONYMOUS_BOARD(40406, HttpStatus.NOT_FOUND, "익명 게시글을 찾을 수 없습니다."),

    /**
     * == 409 CONFLICT ==
     */
    // 중복관련
    DUPLICATE_VALUE(40900, HttpStatus.CONFLICT, "이미 존재하는 항목입니다."),

    // 닉네임 중복
    DUPLICATE_NICKNAME(40901, HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),

    // CP 검색 카테고리 중복
    DUPLICATE_CP_SEARCH_CATEGORY_NAME(40901, HttpStatus.CONFLICT, "이미 존재하는 CP 검색 카테고리 입니다."),
    DUPLICATE_CP_SEARCH_CATEGORY_DATA_NAME(40902, HttpStatus.CONFLICT, "이미 존재하는 CP 검색 카테고리 데이터 입니다."),

    /**
     * == 500 INTERNAL_SERVER_ERROR ==
     */
    // 서버오류
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다."),
    INTERNAL_SERVER_IO_UPLOAD_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    INTERNAL_DATABASE_ERROR(50002, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 업로드 오류입니다."),
    INTERNAL_SERVER_IO_DELETE_ERROR(50003, HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다."),
    INTERNAL_DATA_ACCESS_ERROR(50003, HttpStatus.INTERNAL_SERVER_ERROR, "데이터 접근 오류입니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
