package sixgaezzang.sidepeek.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.redis")
public record RedisProperties(
    String host,
    int port
) {

}
