package sixgaezzang.sidepeek.users.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.Provider;
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
    public Long signUp(SignUpRequest request, Provider provider) {
        verifyUniqueEmail(request);
        verifyUniqueNickname(request);

        User.UserBuilder userBuilder = User.builder()
            .provider(provider)
            .email(request.email())
            .nickname(request.nickname());

        if (Provider.isBasic(provider)) {
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
