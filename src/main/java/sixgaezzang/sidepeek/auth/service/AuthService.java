package sixgaezzang.sidepeek.auth.service;

import jakarta.persistence.EntityNotFoundException;
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
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JWTManager jwtManager;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        if (!user.checkPassword(request.password(), passwordEncoder)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return createTokens(user);
    }

    public UserSummary loadUser(@Login Long loginId) {
        User user = userRepository.findById(loginId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        return UserSummary.from(user);
    }

    public LoginResponse reissue(ReissueTokenRequest request) {
        String refreshToken = request.refreshToken();
        Long userId = jwtManager.getUserId(refreshToken);
        RefreshToken redisRefreshToken = refreshTokenService.getById(userId);

        validateRefreshToken(redisRefreshToken.refreshToken(), refreshToken);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        return createTokens(user);
    }

    @Transactional
    public LoginResponse createTokens(User user) {
        String accessToken = jwtManager.generateAccessToken(user.getId());
        String refreshToken = jwtManager.generateRefreshToken(user.getId());
        refreshTokenService.save(refreshToken);

        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(UserSummary.from(user))
            .build();
    }

    private void validateRefreshToken(String redisRefreshToken, String refreshToken) {
        if (!redisRefreshToken.equals(refreshToken)) {
            throw new InvalidAuthenticationException("유효하지 않은 토큰입니다.");
        }
    }
}
