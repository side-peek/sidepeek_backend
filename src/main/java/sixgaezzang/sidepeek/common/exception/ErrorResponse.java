package sixgaezzang.sidepeek.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "에러 응답")
public record ErrorResponse(
    @Schema(description = "HTTP 상태 코드", example = "BAD_REQUEST")
    HttpStatus status,
    @Schema(description = "에러 코드", example = "400")
    int code,
    @Schema(description = "에러 메시지", example = "유효하지 않은 요청입니다.")
    String message
) {

    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(status, status.value(), message);
    }
}
