package sixgaezzang.sidepeek.users.service;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateEmail;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.DUPLICATE_EMAIL;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.DUPLICATE_NICKNAME;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.EXCESSIVE_NICKNAME_LENGTH;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.INVALID_EMAIL_FORMAT;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import jakarta.persistence.EntityExistsException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.request.SignUpRequest;
import sixgaezzang.sidepeek.users.dto.response.CheckDuplicateResponse;
import sixgaezzang.sidepeek.users.dto.response.UserSearchResponse;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(SignUpRequest request) {
        verifyUniqueEmail(request.email());
        verifyUniqueNickname(request.nickname());

        Password encodedPassword = new Password(request.password(), passwordEncoder);
        User user = User.builder()
            .email(request.email())
            .nickname(request.nickname())
            .password(encodedPassword)
            .build();

        userRepository.save(user);

        return user.getId();
    }

    public UserSearchResponse searchByNickname(String keyword) {
        if (Objects.isNull(keyword) || keyword.isBlank()) {
            return UserSearchResponse.from(userRepository.findAll());
        }

        validateMaxLength(keyword, MAX_NICKNAME_LENGTH, EXCESSIVE_NICKNAME_LENGTH.getMessage());

        return UserSearchResponse.from(userRepository.findAllByNicknameContaining(keyword));
    }

    public CheckDuplicateResponse checkEmailDuplicate(String email) {
        validateEmail(email, INVALID_EMAIL_FORMAT.getMessage());

        boolean isExists = userRepository.existsByEmail(email);
        return new CheckDuplicateResponse(isExists);
    }

    public CheckDuplicateResponse checkNicknameDuplicate(String nickname) {
        validateMaxLength(nickname, MAX_NICKNAME_LENGTH,
            EXCESSIVE_NICKNAME_LENGTH.getMessage());

        boolean isExists = userRepository.existsByNickname(nickname);
        return new CheckDuplicateResponse(isExists);
    }

    private void verifyUniqueNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new EntityExistsException(DUPLICATE_NICKNAME.getMessage());
        }
    }

    private void verifyUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException(DUPLICATE_EMAIL.getMessage());
        }
    }
}
