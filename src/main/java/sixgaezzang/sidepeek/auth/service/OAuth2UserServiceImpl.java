package sixgaezzang.sidepeek.auth.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.auth.domain.ProviderType;
import sixgaezzang.sidepeek.auth.dto.OAuth2UserImpl;
import sixgaezzang.sidepeek.auth.repository.AuthProviderRepository;
import sixgaezzang.sidepeek.auth.service.strategy.UserInfoMapper;
import sixgaezzang.sidepeek.auth.service.strategy.UserInfoMapperFactory;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;
    private final UserInfoMapperFactory userInfoMapperFactory;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        ProviderType providerType = extractProviderType(userRequest);
        UserInfoMapper userInfoMapper = userInfoMapperFactory.getMapper(providerType);
        String providerId = userInfoMapper.getProviderId(attributes);
        AuthProvider provider = getOrCreateAuthProvider(providerType, providerId, attributes,
            userInfoMapper);

        return buildOAuth2User(oauth2User, provider, attributes, userRequest);
    }

    private ProviderType extractProviderType(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        return ProviderType.from(clientRegistration.getClientName());
    }

    private AuthProvider getOrCreateAuthProvider(ProviderType providerType, String providerId,
        Map<String, Object> attributes, UserInfoMapper userInfoMapper) {
        return authProviderRepository.findByProviderTypeAndProviderId(providerType, providerId)
            .orElseGet(
                () -> createAuthProvider(attributes, providerType, providerId, userInfoMapper));
    }

    private AuthProvider createAuthProvider(Map<String, Object> attributes,
        ProviderType providerType, String providerId, UserInfoMapper userInfoMapper) {
        User user = userInfoMapper.mapToUser(attributes);
        userRepository.save(user);

        AuthProvider authProvider = AuthProvider.builder()
            .providerType(providerType)
            .providerId(providerId)
            .user(user)
            .isRegistrationComplete(user.getEmail() != null && user.getNickname() != null)
            .build();

        return authProviderRepository.save(authProvider);
    }

    private OAuth2User buildOAuth2User(OAuth2User oauth2User, AuthProvider provider,
        Map<String, Object> attributes, OAuth2UserRequest userRequest) {
        return OAuth2UserImpl.builder()
            .user(provider.getUser())
            .authProvider(provider)
            .attributes(attributes)
            .nameAttributeKey(userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName())
            .authorities(oauth2User.getAuthorities())
            .build();
    }

}
