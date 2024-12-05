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
    UNAUTHORIZED_USER(40300, HttpStatus.FORBIDDEN, "자신의 게시물이 아닙니다."),


    /**
     * == 404 NOT_FOUND ==
     */
    // 사용자 (user)
    NOT_FOUND_USER(40400, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_END_POINT(40401, HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),

    NOT_FOUND_CP_OPINION(40402, HttpStatus.NOT_FOUND, "CP 의견을 찾을 수 없습니다."),

    /**
     * == 409 CONFLICT ==
     */
    // 중복관련
    DUPLICATE_VALUE(40900, HttpStatus.CONFLICT, "이미 존재하는 항목입니다."),

    // 닉네임 중복
    DUPLICATE_NICKNAME(40901, HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),

    /**
     * == 500 INTERNAL_SERVER_ERROR ==
     */
    // 서버오류
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다."),
    INTERNAL_SERVER_IO_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");


    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
