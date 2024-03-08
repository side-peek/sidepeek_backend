package sixgaezzang.sidepeek.auth.service.strategy;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import sixgaezzang.sidepeek.auth.domain.ProviderType;

@Component
public class UserInfoMapperFactory {

    private Map<ProviderType, UserInfoMapper> mappers;

    public UserInfoMapperFactory() {
        mappers = new HashMap<>();
        mappers.put(ProviderType.GITHUB, new GithubUserInfoMapper());
    }

    public UserInfoMapper getMapper(ProviderType providerType) {
        return mappers.get(providerType);
    }
}
