package sixgaezzang.sidepeek.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

//@Profile({"dev", "prod"})
@ConfigurationProperties(prefix = "slack.webhook")
public record SlackProperties(
    String url
) {
}
