package mediHub_be.common.response;
import com.fasterxml.jackson.annotation.JsonIgnore;
import mediHub_be.common.exception.CustomException;
import mediHub_be.common.exception.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;


public record ApiResponse<T>(
        @JsonIgnore
        HttpStatus httpStatus,
        boolean success,
        @Nullable T data,
        @Nullable ExceptionDTO error
) {
    // 자주 쓰이는 200 응답
    public static <T> ApiResponse<T> ok(@Nullable final T data) {
        return new ApiResponse<>(HttpStatus.OK, true, data, null);
    }

    // 자주 쓰이는 201 응답
    public static <T> ApiResponse<T> created(@Nullable final T data) {
        return new ApiResponse<>(HttpStatus.CREATED, true, data, null);
    }

    // 실패 응답
    public static <T> ApiResponse<T> fail(final CustomException e) {
        return new ApiResponse<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDTO.of(e.getErrorCode()));
    }
}
