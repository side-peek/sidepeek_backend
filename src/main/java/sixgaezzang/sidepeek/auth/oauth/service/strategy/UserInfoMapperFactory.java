package sixgaezzang.sidepeek.auth.oauth.service.strategy;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import sixgaezzang.sidepeek.auth.domain.ProviderType;
import sixgaezzang.sidepeek.users.service.UserService;

@Component
public class UserInfoMapperFactory {

    private Map<ProviderType, UserInfoMapper> mappers;

    public UserInfoMapperFactory(UserService userService) {
        mappers = new HashMap<>();
        mappers.put(ProviderType.GITHUB, new GithubUserInfoMapper(userService));
    }

    public UserInfoMapper getMapper(ProviderType providerType) {
        return mappers.get(providerType);
    }
}
