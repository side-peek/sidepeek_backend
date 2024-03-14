package sixgaezzang.sidepeek.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.oauth2.redirect")
public record OAuth2RedirectUriProperties(
    String basicUri,
    String registrationUri
) {

}
