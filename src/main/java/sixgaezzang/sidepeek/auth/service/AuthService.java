package sixgaezzang.sidepeek.auth.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.auth.dto.request.LoginRequest;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;
import sixgaezzang.sidepeek.auth.jwt.JWTManager;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummaryResponse;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTManager jwtManager;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        if (!user.checkPassword(request.password(), passwordEncoder)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtManager.generateAccessToken(user.getId());

        return LoginResponse.builder()
            .accessToken(accessToken)
            .user(UserSummaryResponse.from(user))
            .build();
    }

    public UserSummaryResponse loadUser(Long loginId) {
        User user = userRepository.findById(loginId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        return UserSummaryResponse.from(user);
    }
}
