package sixgaezzang.sidepeek.auth.oauth.handler;

import static sixgaezzang.sidepeek.auth.exception.message.AuthErrorMessage.OAUTH_USER_TYPE_IS_INVALID;
import static sixgaezzang.sidepeek.common.util.ResponseUtils.sendResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;
import sixgaezzang.sidepeek.auth.dto.response.SocialLoginResponse;
import sixgaezzang.sidepeek.auth.oauth.OAuth2UserImpl;
import sixgaezzang.sidepeek.auth.service.AuthService;
import sixgaezzang.sidepeek.users.domain.User;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        OAuth2UserImpl oauth2User = extractOAuth2User(authentication);
        User user = oauth2User.getUser();
        AuthProvider authProvider = oauth2User.getAuthProvider();

        SocialLoginResponse socialLoginResponse = createSocialLoginResponse(user, authProvider);

        sendResponse(HttpStatus.OK, socialLoginResponse, response);
    }

    private OAuth2UserImpl extractOAuth2User(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof OAuth2UserImpl oauth2User)) {
            throw new InternalAuthenticationServiceException(OAUTH_USER_TYPE_IS_INVALID);
        }
        return oauth2User;
    }

    private SocialLoginResponse createSocialLoginResponse(User user, AuthProvider authProvider) {
        LoginResponse loginResponse = authService.createTokens(user);
        return SocialLoginResponse.of(loginResponse, authProvider);
    }
}
