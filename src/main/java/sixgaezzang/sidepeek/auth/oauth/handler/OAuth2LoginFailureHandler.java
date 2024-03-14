package sixgaezzang.sidepeek.auth.oauth.handler;

import static sixgaezzang.sidepeek.common.util.ResponseUtils.sendErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException e) {
        sendErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), response);
    }
}
