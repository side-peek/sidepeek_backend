package sixgaezzang.sidepeek.config;

import com.slack.api.Slack;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sixgaezzang.sidepeek.common.util.component.SlackClient;
import sixgaezzang.sidepeek.config.properties.SlackProperties;

//@Profile({"dev", "prod"})
@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SlackConfig {

    private final SlackProperties slackProperties;

    @Bean
    SlackClient slackClient(SlackProperties slackProperties) {
        return new SlackClient(Slack.getInstance(), slackProperties);
    }
}
