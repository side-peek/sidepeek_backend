package sixgaezzang.sidepeek.common.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        List<ErrorResponse> responses = convertToErrorResponses(e);
        log.debug(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(responses);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<List<ErrorResponse>> handleHandlerMethodValidationException(
        HandlerMethodValidationException e) {
        List<ErrorResponse> responses = convertToErrorResponses(e);
        log.debug(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(responses);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e
    ) {
        String message = formatMessageFrom(e);
        ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
        log.debug(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.NOT_FOUND, e.getMessage());
        log.debug(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityExistsException(EntityExistsException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.CONFLICT, e.getMessage());
        log.debug(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
        log.debug(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAuthenticationException(
        InvalidAuthenticationException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage());
        log.warn(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(errorResponse);
    }

    @ExceptionHandler(InvalidAuthorityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAuthorityException(
        InvalidAuthorityException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, e.getMessage());
        log.warn(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
            "API 요청 형식이 올바르지 않습니다.");
        log.debug(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

    @ExceptionHandler(TokenValidationFailException.class)
    public ResponseEntity<ErrorResponse> handleTokenValidationFailException(
        TokenValidationFailException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage());
        log.warn(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, e.getMessage());
        log.warn(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR,
            e.getMessage());
        log.error(e.getMessage(), e.fillInStackTrace());

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
    }

    private List<ErrorResponse> convertToErrorResponses(HandlerMethodValidationException e) {
        return e.getAllErrors()
            .stream()
            .map(fieldError -> ErrorResponse.of(HttpStatus.BAD_REQUEST,
                fieldError.getDefaultMessage()))
            .toList();
    }

    private List<ErrorResponse> convertToErrorResponses(MethodArgumentNotValidException e) {
        return e.getFieldErrors()
            .stream()
            .map(fieldError -> ErrorResponse.of(HttpStatus.BAD_REQUEST,
                fieldError.getDefaultMessage()))
            .toList();
    }

    private String formatMessageFrom(MethodArgumentTypeMismatchException e) {
        String parameterName = e.getParameter()
            .getParameterName();

        return parameterName + "의 형식이 유효하지 않습니다.";
    }
}
