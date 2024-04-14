package sixgaezzang.sidepeek.auth.oauth.service.strategy;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import sixgaezzang.sidepeek.auth.domain.ProviderType;
import sixgaezzang.sidepeek.auth.oauth.service.strategy.github.GithubUserInfoMapper;

@Component
public class UserInfoMapperFactory {

    private final Map<ProviderType, UserInfoMapper> mappers;

    public UserInfoMapperFactory() {
        mappers = new HashMap<>();
        mappers.put(ProviderType.GITHUB, new GithubUserInfoMapper());
    }

    public UserInfoMapper getMapper(ProviderType providerType) {
        return mappers.get(providerType);
    }
}
