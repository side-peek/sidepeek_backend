package sixgaezzang.sidepeek.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

//@Profile({"dev", "prod"})
@ConfigurationProperties(prefix = "sentry")
public record SentryAlertProperties(
    String environment,
    String issueBaseUrl
) {
}
