package mediHub_be.common.exception;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ExceptionDTO {
    @NotNull
    private final Integer code;

    @NotNull
    private final String message;

    public ExceptionDTO(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static ExceptionDTO of(ErrorCode errorCode) {
        return new ExceptionDTO(errorCode);
    }
}
