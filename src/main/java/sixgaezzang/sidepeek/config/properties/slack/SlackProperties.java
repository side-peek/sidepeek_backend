package sixgaezzang.sidepeek.config.properties.slack;

import org.springframework.boot.context.properties.ConfigurationProperties;

//@Profile({"dev", "prod"})
@ConfigurationProperties(prefix = "slack.webhook")
public record SlackProperties(
    SlackErrorImageProperties errorImage,
    String url
) {
    public String getErrorImageUrl() {
        return errorImage.url();
    }

    public String getErrorImageAlt() {
        return errorImage.alt();
    }
}
