package sixgaezzang.sidepeek.users.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.users.domain.LoginType;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.SignUpRequest;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EntityExistsException("이미 사용 중인 이메일입니다.");
        }

        if (userRepository.existsByNickname(request.nickname())) {
            throw new EntityExistsException("이미 사용 중인 닉네임입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
            .loginType(LoginType.EMAIL)
            .email(request.email())
            .password(encodedPassword)
            .nickname(request.nickname())
            .build();

        User saved = userRepository.save(user);

        return saved.getId();
    }

}
