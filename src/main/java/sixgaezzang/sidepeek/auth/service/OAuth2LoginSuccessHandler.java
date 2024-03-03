package sixgaezzang.sidepeek.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.auth.dto.OAuth2UserImpl;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;
import sixgaezzang.sidepeek.auth.dto.response.SocialLoginResponse;
import sixgaezzang.sidepeek.users.domain.User;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        OAuth2UserImpl oAuth2User = (OAuth2UserImpl) authentication.getPrincipal();
        User user = oAuth2User.getUser();
        AuthProvider authProvider = oAuth2User.getAuthProvider();
        
        LoginResponse loginResponse = authService.createTokens(user);
        SocialLoginResponse socialLoginResponse = SocialLoginResponse.of(loginResponse,
            authProvider);

        writeResponseAsJson(response, socialLoginResponse);
    }

    private void writeResponseAsJson(HttpServletResponse response,
        SocialLoginResponse socialLoginResponse) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getOutputStream(), socialLoginResponse);
    }
}
