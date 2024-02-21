package sixgaezzang.sidepeek.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import sixgaezzang.sidepeek.common.exception.TokenValidationFailException;
import sixgaezzang.sidepeek.config.properties.JWTProperties;

@Component
public class JWTManager {

    private static final int SECONDS_PER_MINUTE = 60;
    private static final long MILLISECONDS_PER_SECOND = 1000L;
    public static final String USER_ID_CLAIM = "user_id";

    private final String issuer;
    private final SecretKey secretKey;
    private final long expiredAfter;

    public JWTManager(JWTProperties jwtProperties) {
        this.issuer = jwtProperties.issuer();
        this.secretKey = Keys.hmacShaKeyFor(
            jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
        this.expiredAfter =
            jwtProperties.expiredAfter() * SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;
    }

    public String generateAccessToken(long userId) {
        return generateToken(userId, expiredAfter);
    }

    public Long getUserId(String token) {
        return parseClaims(token).get(USER_ID_CLAIM, Long.class);
    }

    public Long getExpiredAt(String token) {
        return parseClaims(token).getExpiration().getTime();
    }

    private String generateToken(long userId, long expiredAfter) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + expiredAfter);

        return Jwts.builder()
            .setIssuer(issuer)
            .setIssuedAt(now)
            .setExpiration(expiredAt)
            .claim(USER_ID_CLAIM, userId)
            .signWith(secretKey)
            .compact();
    }
}
