package sixgaezzang.sidepeek.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sixgaezzang.sidepeek.config.properties.JWTProperties;

@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter {

    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    public final String JWT_KEY;

    public JWTValidationFilter(JWTProperties jwtProperties) {
        JWT_KEY = jwtProperties.secretKey();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(JWT_HEADER);
        String jwt = resolveToken(bearerToken);

        if (null != jwt) {
            try {
                SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt)
                    .getBody();
                String userId = String.valueOf(claims.get(JWTManager.USER_ID_CLAIM));
                Authentication auth = new UsernamePasswordAuthenticationToken(userId, null);

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadCredentialsException("유효하지 않은 토큰입니다.");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JWT_PREFIX)) {
            return bearerToken.substring(JWT_PREFIX.length());
        }

        return null;
    }

}
