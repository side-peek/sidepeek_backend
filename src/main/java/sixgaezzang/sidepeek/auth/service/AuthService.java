package sixgaezzang.sidepeek.auth.service;

import static sixgaezzang.sidepeek.auth.exeption.message.AuthErrorMessage.PASSWORD_NOT_MATCH;
import static sixgaezzang.sidepeek.auth.exeption.message.AuthErrorMessage.TOKEN_IS_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.auth.domain.RefreshToken;
import sixgaezzang.sidepeek.auth.dto.request.LoginRequest;
import sixgaezzang.sidepeek.auth.dto.request.ReissueTokenRequest;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;
import sixgaezzang.sidepeek.auth.jwt.JWTManager;
import sixgaezzang.sidepeek.auth.repository.AuthProviderRepository;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

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
            throw new InvalidAuthenticationException(TOKEN_IS_INVALID);
        }
    }
}
