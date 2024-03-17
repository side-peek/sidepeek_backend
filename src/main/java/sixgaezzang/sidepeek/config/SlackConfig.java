package sixgaezzang.sidepeek.config;

import com.slack.api.Slack;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SlackConfig {
    @Bean
    Slack slack() {
        return Slack.getInstance();
    }
}
