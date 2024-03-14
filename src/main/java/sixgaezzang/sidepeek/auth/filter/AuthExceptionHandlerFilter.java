package sixgaezzang.sidepeek.auth.filter;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static sixgaezzang.sidepeek.common.util.ResponseUtils.sendErrorResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class AuthExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            log.debug(e.getMessage(), e.fillInStackTrace());
            sendErrorResponse(UNAUTHORIZED, e.getMessage(), response);
        }
    }

}
