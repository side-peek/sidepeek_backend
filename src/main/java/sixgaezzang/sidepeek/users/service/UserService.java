package sixgaezzang.sidepeek.users.service;

import jakarta.persistence.EntityExistsException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.users.domain.LoginType;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.SignUpRequest;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signUp(SignUpRequest request) {
        return signUp(request, Optional.empty());
    }

    public Long signUp(SignUpRequest request, LoginType loginType) {
        return signUp(request, Optional.of(loginType));
    }

    @Transactional
    public Long signUp(SignUpRequest request, Optional<LoginType> optionalLoginType) {
        verifyUniqueEmail(request);
        verifyUniqueNickname(request);

        User.UserBuilder userBuilder = User.builder()
            .loginType(optionalLoginType.orElse(LoginType.EMAIL))
            .email(request.email())
            .nickname(request.nickname());

        if (optionalLoginType.isEmpty()) {
            Password encodedPassword = new Password(request.password(), passwordEncoder);
            userBuilder.password(encodedPassword);
        }

        User user = userBuilder.build();
        User saved = userRepository.save(user);

        return saved.getId();
    }

    private void verifyUniqueNickname(SignUpRequest request) {
        if (userRepository.existsByNickname(request.nickname())) {
            throw new EntityExistsException("이미 사용 중인 닉네임입니다.");
        }
    }

    private void verifyUniqueEmail(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EntityExistsException("이미 사용 중인 이메일입니다.");
        }
    }

}
