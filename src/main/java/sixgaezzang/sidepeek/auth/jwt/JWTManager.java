package sixgaezzang.sidepeek.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
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
        Instant now = Instant.now();
        Instant expiredAt = now.plusMillis(expiredAfter);

        return Jwts.builder()
            .setIssuer(issuer)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiredAt))
            .claim(USER_ID_CLAIM, userId)
            .signWith(secretKey)
            .compact();
    }
}
