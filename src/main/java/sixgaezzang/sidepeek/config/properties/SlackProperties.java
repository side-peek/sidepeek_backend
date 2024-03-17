package sixgaezzang.sidepeek.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "slack.webhook")
public record SlackProperties(
    String url
) {
}
