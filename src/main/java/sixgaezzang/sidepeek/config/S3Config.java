package sixgaezzang.sidepeek.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sixgaezzang.sidepeek.config.properties.S3Properties;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@ConfigurationPropertiesScan(basePackages = "sixgaezzang.sidepeek.media.util")
@RequiredArgsConstructor
public class S3Config {

    private final S3Properties s3Properties;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.of(s3Properties.region()))
            .build();
    }

}
