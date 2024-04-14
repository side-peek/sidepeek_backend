package sixgaezzang.sidepeek.auth.service;

import static sixgaezzang.sidepeek.auth.exception.message.AuthErrorMessage.PASSWORD_NOT_MATCH;
import static sixgaezzang.sidepeek.auth.exception.message.AuthErrorMessage.TOKEN_IS_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.auth.domain.ProviderType;
import sixgaezzang.sidepeek.auth.domain.RefreshToken;
import sixgaezzang.sidepeek.auth.dto.request.LoginRequest;
import sixgaezzang.sidepeek.auth.dto.request.ReissueTokenRequest;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;
import sixgaezzang.sidepeek.auth.jwt.JWTManager;
import sixgaezzang.sidepeek.auth.oauth.OAuth2User;
import sixgaezzang.sidepeek.auth.oauth.service.strategy.OAuth2Manager;
import sixgaezzang.sidepeek.auth.oauth.service.strategy.OAuth2ManagerFactory;
import sixgaezzang.sidepeek.auth.repository.AuthProviderRepository;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.common.exception.TokenValidationFailException;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final OAuth2ManagerFactory oauth2ManagerFactory;
    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JWTManager jwtManager;

    public LoginResponse login(LoginRequest request) {
        User user = getUserById(userRepository.findByEmail(request.email()));

        if (!user.checkPassword(request.password(), passwordEncoder)) {
            throw new BadCredentialsException(PASSWORD_NOT_MATCH);
        }

        return createTokens(user);
    }

    public UserSummary loadUser(@Login Long loginId) {
        User user = getUserById(userRepository.findById(loginId));
        boolean isSocialLogin = authProviderRepository.existsByUser(user);

        return UserSummary.fromWithIsSocialLogin(user, isSocialLogin);
    }

    public LoginResponse reissue(ReissueTokenRequest request) {
        String refreshToken = request.refreshToken();
        Long userId = jwtManager.getUserId(refreshToken);
        RefreshToken redisRefreshToken = refreshTokenService.getById(userId);

        validateRefreshToken(redisRefreshToken.refreshToken(), refreshToken);

        User user = getUserById(userRepository.findById(userId));

        return createTokens(user);
    }

    @Transactional
    public LoginResponse socialLogin(String provider, String code) {
        ProviderType providerType = ProviderType.valueOf(provider.toUpperCase());
        OAuth2Manager manager = oauth2ManagerFactory.getManager(providerType);
        OAuth2User oauth2User = manager.getOauth2User(code);

        AuthProvider authProvider = getAuthProvider(oauth2User);
        User user = authProvider.getUser();

        return createTokens(user);
    }

    @Transactional
    public LoginResponse createTokens(User user) {
        String accessToken = jwtManager.generateAccessToken(user.getId());
        String refreshToken = jwtManager.generateRefreshToken(user.getId());
        refreshTokenService.save(refreshToken);

        boolean isSocialLogin = authProviderRepository.existsByUser(user);

        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(UserSummary.fromWithIsSocialLogin(user, isSocialLogin))
            .build();
    }

    private User getUserById(Optional<User> userRepository) {
        return userRepository
            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXISTING));
    }

    private void validateRefreshToken(String redisRefreshToken, String refreshToken) {
        if (!redisRefreshToken.equals(refreshToken)) {
            throw new TokenValidationFailException(TOKEN_IS_INVALID);
        }
    }

    private AuthProvider getAuthProvider(OAuth2User oauth2User) {
        AuthProvider authProvider = oauth2User.getAuthProvider();
        return authProviderRepository.findByProviderTypeAndProviderId(
                authProvider.getProviderType(), authProvider.getProviderId())
            .orElseGet(() -> createUserAndAuthProvider(oauth2User));
    }

    private AuthProvider createUserAndAuthProvider(OAuth2User oauth2User) {
        AuthProvider oauth2AuthProvider = oauth2User.getAuthProvider();
        User user = oAuth2UserToUser(oauth2User);

        userRepository.save(user);

        AuthProvider authProvider = AuthProvider.builder()
            .user(user)
            .providerType(oauth2AuthProvider.getProviderType())
            .providerId(oauth2AuthProvider.getProviderId())
            .build();

        return authProviderRepository.save(authProvider);
    }

    private User oAuth2UserToUser(OAuth2User oauth2User) {
        User user = oauth2User.getUser();
        String email = userRepository.existsByEmail(user.getEmail()) ? null : user.getEmail();
        String nickname =
            userRepository.existsByNickname(user.getNickname()) ? null : user.getNickname();

        return User.builder()
            .email(email)
            .nickname(nickname)
            .profileImageUrl(oauth2User.getUser().getProfileImageUrl())
            .build();
    }
}
