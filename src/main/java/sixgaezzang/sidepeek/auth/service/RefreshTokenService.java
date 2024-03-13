package sixgaezzang.sidepeek.auth.service;

import static sixgaezzang.sidepeek.auth.exeption.message.AuthErrorMessage.TOKEN_IS_INVALID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sixgaezzang.sidepeek.auth.domain.RefreshToken;
import sixgaezzang.sidepeek.auth.jwt.JWTManager;
import sixgaezzang.sidepeek.auth.repository.RefreshTokenRepository;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTManager jwtManager;

    public RefreshToken save(String token) {
        Long userId = jwtManager.getUserId(token);
        Long expirationAt = jwtManager.getExpiredAt(token);
        RefreshToken refreshToken = RefreshToken.from(userId, token, expirationAt);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getById(Long userId) {
        return refreshTokenRepository.findById(userId)
            .orElseThrow(() -> new InvalidAuthenticationException(TOKEN_IS_INVALID));
    }
}
