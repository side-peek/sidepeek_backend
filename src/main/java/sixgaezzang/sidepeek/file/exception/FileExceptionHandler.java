package sixgaezzang.sidepeek.file.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class FileExceptionHandler {
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
        HttpMediaTypeNotSupportedException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
            "지원하는 Content-Type이 아닙니다.");

        return ResponseEntity.badRequest()
            .body(errorResponse);
    }

    @ExceptionHandler({MultipartException.class, MissingServletRequestPartException.class})
    public ResponseEntity<ErrorResponse> handleMultipartRequestExceptions(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
            "요청에 파일이 누락되어 있는지 확인해주세요.");

        return ResponseEntity.badRequest()
            .body(errorResponse);
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponse> handleS3Exception(S3Exception e) {
        log.error(e.getMessage(), e.fillInStackTrace());
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_GATEWAY,
            "파일을 저장하는 동안 문제가 발생했습니다.");

        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(errorResponse);
    }
}
