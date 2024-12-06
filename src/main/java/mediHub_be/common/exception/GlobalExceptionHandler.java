package mediHub_be.common.exception;

import lombok.extern.slf4j.Slf4j;
import mediHub_be.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 존재하지 않는 요청에 대한 예외
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ApiResponse<?>> handleNoPageFoundException(Exception e) {
        log.error("GlobalExceptionHandler catch NoHandlerFoundException : {}", e.getMessage());
        ApiResponse<?> apiResponse = ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND_END_POINT));
        return ResponseEntity.status(ErrorCode.NOT_FOUND_END_POINT.getHttpStatus())
                .body(apiResponse);
    }

    // 커스텀 예외
    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        log.error("handleCustomException() in GlobalExceptionHandler throw CustomException : {}", e.getMessage());
        ApiResponse<?> apiResponse = ApiResponse.fail(e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(apiResponse);
    }

    // 기본 예외
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("handleException() in GlobalExceptionHandler throw Exception : {}", e.getMessage());
        e.printStackTrace();

        ApiResponse<?> apiResponse = ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(apiResponse);
    }
}
