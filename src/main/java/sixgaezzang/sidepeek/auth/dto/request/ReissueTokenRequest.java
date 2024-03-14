package sixgaezzang.sidepeek.auth.dto.request;

import static sixgaezzang.sidepeek.auth.exception.message.AuthErrorMessage.REFRESH_TOKEN_IS_NULL;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "토큰 재발급 요청")
public record ReissueTokenRequest(
    @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzaXhnYWV6emFuZyIsImlhdCI6MTcwODc3OTc1OSwiZXhwIjoxNzA5Mzg0NTU5LCJ1c2VyX2lkIjozfQ.lDZaPmXFWfN6z4p6pWdNMA8coTxGNJjadC_X1liEMsZRkote7NaD4cemZnU5ag3tgH5y0SOeXTJkBO11aENVnw")
    @NotBlank(message = REFRESH_TOKEN_IS_NULL)
    String refreshToken
) {

}
