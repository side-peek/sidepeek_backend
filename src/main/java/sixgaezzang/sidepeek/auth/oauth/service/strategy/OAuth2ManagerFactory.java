package sixgaezzang.sidepeek.auth.oauth.service.strategy;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import sixgaezzang.sidepeek.auth.domain.ProviderType;
import sixgaezzang.sidepeek.auth.oauth.service.strategy.github.GithubOAuth2Manager;
import sixgaezzang.sidepeek.config.properties.GithubOAuth2Properties;

@Component
public class OAuth2ManagerFactory {

    private final Map<ProviderType, OAuth2Manager> mappers;

    public OAuth2ManagerFactory(GithubOAuth2Properties githubOAuth2Properties) {
        mappers = new HashMap<>();
        mappers.put(ProviderType.GITHUB, new GithubOAuth2Manager(githubOAuth2Properties));
    }

    public OAuth2Manager getManager(ProviderType providerType) {
        return mappers.get(providerType);
    }

}
