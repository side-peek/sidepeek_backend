package sixgaezzang.sidepeek.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JWTProperties(
    String issuer,
    String secretKey,
    int expiredAfter,
    int refreshExpiredAfter
) {

}
