package sixgaezzang.sidepeek.common.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
    HttpStatus status,
    int code,
    String message
) {

    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(status, status.value(), message);
    }
}
