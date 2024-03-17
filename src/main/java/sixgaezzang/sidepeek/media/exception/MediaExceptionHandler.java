package sixgaezzang.sidepeek.media.exception;

import static sixgaezzang.sidepeek.media.exception.message.MediaErrorMessage.CANNOT_READ_FILE;
import static sixgaezzang.sidepeek.media.exception.message.MediaErrorMessage.CONTENT_TYPE_IS_UNSUPPORTED;
import static sixgaezzang.sidepeek.media.exception.message.MediaErrorMessage.FILE_IS_EMPTY;

import io.sentry.Sentry;
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
public class MediaExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
        HttpMediaTypeNotSupportedException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
            CONTENT_TYPE_IS_UNSUPPORTED);
        log.debug(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.badRequest()
            .body(errorResponse);
    }

    @ExceptionHandler({MultipartException.class, MissingServletRequestPartException.class})
    public ResponseEntity<ErrorResponse> handleMultipartRequestExceptions(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
            FILE_IS_EMPTY);
        log.debug(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.badRequest()
            .body(errorResponse);
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponse> handleS3Exception(S3Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_GATEWAY,
            CANNOT_READ_FILE);
        log.error(e.getMessage(), e.fillInStackTrace());
        Sentry.captureException(e);

        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(errorResponse);
    }
}
