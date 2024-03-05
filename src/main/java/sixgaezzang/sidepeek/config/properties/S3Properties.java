package sixgaezzang.sidepeek.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
public record S3Properties(
    String region,
    String baseBucket,
    String basePath
) {
}
