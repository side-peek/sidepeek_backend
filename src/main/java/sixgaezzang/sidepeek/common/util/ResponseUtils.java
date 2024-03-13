package sixgaezzang.sidepeek.common.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.ERROR_OCCURRED_SENDING_RESPONSE;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.ERROR_OCCURRED_WRITING_JSON;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendErrorResponse(HttpStatus httpStatus, String message,
        HttpServletResponse response) {
        sendResponse(httpStatus.value(), ErrorResponse.of(httpStatus, message), response);
    }

    private static void sendResponse(int httpStatusCode, Object responseBody,
        HttpServletResponse response) {
        try {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(UTF_8.name());
            response.setStatus(httpStatusCode);
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        } catch (IOException e) {
            log.error(ERROR_OCCURRED_WRITING_JSON, e);
            sendInternalServerError(response);
        }
    }

    private static void sendInternalServerError(HttpServletResponse response) {
        try {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR);
            
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(UTF_8.name());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            log.error(ERROR_OCCURRED_SENDING_RESPONSE, e);
        }
    }
}
