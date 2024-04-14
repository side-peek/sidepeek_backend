package sixgaezzang.sidepeek.auth.oauth.service.strategy.github;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static sixgaezzang.sidepeek.auth.domain.ProviderType.GITHUB;
import static sixgaezzang.sidepeek.auth.exception.message.AuthErrorMessage.OAUTH_CREDENTIALS_IS_INVALID;

import java.util.Map;
import java.util.Objects;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.reactive.function.client.WebClient;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.auth.oauth.OAuth2User;
import sixgaezzang.sidepeek.auth.oauth.service.strategy.OAuth2Manager;
import sixgaezzang.sidepeek.config.properties.GithubOAuth2Properties;
import sixgaezzang.sidepeek.users.domain.User;

public class GithubOAuth2Manager implements OAuth2Manager {

    private static final String GITHUB_OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String GITHUB_USER_INFO_URL = "https://api.github.com/user";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String CODE = "code";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final GithubUserInfoMapper mapper;
    private final String clientId;
    private final String clientSecret;

    public GithubOAuth2Manager(GithubOAuth2Properties properties) {
        this.mapper = new GithubUserInfoMapper();
        this.clientId = properties.clientId();
        this.clientSecret = properties.clientSecret();
    }

    @Override
    public OAuth2User getOauth2User(String code) {
        Map<String, Object> userInfo = getUserInfo(code);
        User user = mapper.mapToUser(userInfo);
        AuthProvider authProvider = AuthProvider.builder()
            .providerId(mapper.getProviderId(userInfo))
            .providerType(GITHUB)
            .build();

        return OAuth2User.builder()
            .user(user)
            .authProvider(authProvider)
            .build();
    }

    public Map<String, Object> getUserInfo(String code) {
        String accessToken = getAccessToken(code);

        WebClient webClient = WebClient.builder()
            .baseUrl(GITHUB_USER_INFO_URL)
            .build();

        return webClient.get()
            .header(AUTHORIZATION, TOKEN_PREFIX + accessToken)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
            .doOnError(error -> {
                throw new AuthenticationServiceException(OAUTH_CREDENTIALS_IS_INVALID);
            }).block();
    }

    private String getAccessToken(String code) {
        WebClient webClient = WebClient.builder()
            .baseUrl(GITHUB_OAUTH_ACCESS_TOKEN_URL)
            .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
            .build();

        Map<String, String> result = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .queryParam(CLIENT_ID, clientId)
                .queryParam(CLIENT_SECRET, clientSecret)
                .queryParam(CODE, code)
                .build()
            )
            .retrieve()
            .bodyToMono(
                new ParameterizedTypeReference<Map<String, String>>() {
                }
            )
            .block();

        return Objects.requireNonNull(result)
            .get(ACCESS_TOKEN);
    }
}
