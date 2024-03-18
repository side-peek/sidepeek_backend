package sixgaezzang.sidepeek.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Schema(description = "로그인 응답")
@Builder
public record LoginResponse(
    @Schema(description = "Access 토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzaXhnYWV6emFuZyIsImlhdCI6MTcwODc3OTc1OSwiZXhwIjoxNzA4NzgzMzU5LCJ1c2VyX2lkIjozfQ.KA5BjtJtyYA-SEHKn_E4hTwz6YhZ2rMXsnIflL5zoa_GjBi4KTRiYbveBhUBKq1q4Qfb1HgRg-vy4G9YIg9MVQ")
    String accessToken,
    @Schema(description = "Refresh 토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzaXhnYWV6emFuZyIsImlhdCI6MTcwODc3OTc1OSwiZXhwIjoxNzA5Mzg0NTU5LCJ1c2VyX2lkIjozfQ.lDZaPmXFWfN6z4p6pWdNMA8coTxGNJjadC_X1liEMsZRkote7NaD4cemZnU5ag3tgH5y0SOeXTJkBO11aENVnw")
    String refreshToken,
    @Schema(description = "로그인 사용자 정보")
    UserSummary user
) {

}
