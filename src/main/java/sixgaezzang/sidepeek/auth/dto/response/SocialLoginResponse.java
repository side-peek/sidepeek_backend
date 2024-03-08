package sixgaezzang.sidepeek.auth.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.auth.domain.ProviderType;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Builder
public record SocialLoginResponse(
    String accessToken,
    String refreshToken,
    ProviderType providerType,
    boolean isRegistrationComplete,
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
