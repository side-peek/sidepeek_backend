package sixgaezzang.sidepeek.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.auth.domain.ProviderType;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Schema(description = "소셜 로그인 응답")
@Builder
public record SocialLoginResponse(
    @Schema(description = "Access 토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzaXhnYWV6emFuZyIsImlhdCI6MTcwODc3OTc1OSwiZXhwIjoxNzA4NzgzMzU5LCJ1c2VyX2lkIjozfQ.KA5BjtJtyYA-SEHKn_E4hTwz6YhZ2rMXsnIflL5zoa_GjBi4KTRiYbveBhUBKq1q4Qfb1HgRg-vy4G9YIg9MVQ")
    String accessToken,
    @Schema(description = "Refresh 토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzaXhnYWV6emFuZyIsImlhdCI6MTcwODc3OTc1OSwiZXhwIjoxNzA5Mzg0NTU5LCJ1c2VyX2lkIjozfQ.lDZaPmXFWfN6z4p6pWdNMA8coTxGNJjadC_X1liEMsZRkote7NaD4cemZnU5ag3tgH5y0SOeXTJkBO11aENVnw")
    String refreshToken,
    @Schema(description = "소셜 로그인 타입", example = "GITHUB")
    ProviderType providerType,
    @Schema(description = "회원가입 완료 여부", example = "true")
    boolean isRegistrationComplete,
    @Schema(description = "로그인 사용자 정보")
    UserSummary user
) {

    public static SocialLoginResponse of(LoginResponse loginResponse, AuthProvider authProvider) {
        return SocialLoginResponse.builder()
            .accessToken(loginResponse.accessToken())
            .refreshToken(loginResponse.refreshToken())
            .providerType(authProvider.getProviderType())
            .isRegistrationComplete(authProvider.isRegistrationComplete())
            .user(loginResponse.user())
            .build();
    }

}
