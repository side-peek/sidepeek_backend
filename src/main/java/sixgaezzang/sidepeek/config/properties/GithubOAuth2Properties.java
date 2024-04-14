package sixgaezzang.sidepeek.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth2.github")
public record GithubOAuth2Properties(
    String clientId,
    String clientSecret
) {

}
