package sixgaezzang.sidepeek.users.service;

import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateEmail;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.EMAIL_DUPLICATE;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.EMAIL_FORMAT_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_DUPLICATE;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PASSWORD_IS_SAME_AS_BEFORE;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PASSWORD_NOT_MATCH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateLoginIdEqualsUserId;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateUserId;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.auth.repository.AuthProviderRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.request.SignUpRequest;
import sixgaezzang.sidepeek.users.dto.request.UpdatePasswordRequest;
import sixgaezzang.sidepeek.users.dto.request.UpdateUserProfileRequest;
import sixgaezzang.sidepeek.users.dto.response.CheckDuplicateResponse;
import sixgaezzang.sidepeek.users.dto.response.UserProfileResponse;
import sixgaezzang.sidepeek.users.dto.response.UserSearchResponse;
import sixgaezzang.sidepeek.users.dto.response.UserSkillSummary;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;
    private final UserSkillService userSkillService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(SignUpRequest request) {
        verifyUniqueEmail(request.email());
        verifyUniqueNickname(request.nickname());

        User user = User.builder()
            .email(request.email())
            .nickname(request.nickname())
            .password(request.password())
            .passwordEncoder(passwordEncoder)
            .build();

        userRepository.save(user);

        return user.getId();
    }

    public User getById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXISTING));
    }

    public User getByIdOrNull(Long userId) {
        return Optional.ofNullable(userId)
            .map(this::getById)
            .orElse(null);
    }

    public UserSearchResponse searchByNickname(String keyword) {
        if (Objects.isNull(keyword) || keyword.isBlank()) {
            return UserSearchResponse.from(userRepository.findAll());
        }
        validateMaxLength(keyword, MAX_NICKNAME_LENGTH, NICKNAME_OVER_MAX_LENGTH);

        return UserSearchResponse.from(userRepository.findAllByNicknameContaining(keyword));
    }

    public CheckDuplicateResponse checkEmailDuplicate(String email) {
        validateEmail(email, EMAIL_FORMAT_INVALID);

        boolean isExists = userRepository.existsByEmail(email);

        return new CheckDuplicateResponse(isExists);
    }

    public CheckDuplicateResponse checkNicknameDuplicate(String nickname) {
        validateMaxLength(nickname, MAX_NICKNAME_LENGTH,
            NICKNAME_OVER_MAX_LENGTH);

        boolean isExists = userRepository.existsByNickname(nickname);

        return new CheckDuplicateResponse(isExists);
    }

    public UserProfileResponse getProfileById(Long id) {
        validateUserId(id);

        User user = findUserById(id);

        List<UserSkillSummary> techStacks = userSkillService.findAllByUser(user);

        boolean isSocialLogin = authProviderRepository.existsByUser(user);

        return UserProfileResponse.from(user, isSocialLogin, techStacks);
    }

    @Transactional
    public UserProfileResponse updateProfile(Long loginId, Long id,
        UpdateUserProfileRequest request) {
        validateLoginIdEqualsUserId(loginId, id);

        User user = findUserById(id);

        user.update(request);

        List<UserSkillSummary> techStacks = userSkillService.saveAll(user, request.techStacks());

        boolean isSocialLogin = authProviderRepository.existsByUser(user);

        return UserProfileResponse.from(user, isSocialLogin, techStacks);
    }

    @Transactional
    public void updatePassword(Long loginId, Long userId, UpdatePasswordRequest request) {
        validateLoginIdEqualsUserId(loginId, userId);

        String originalPassword = request.originalPassword();
        String newPassword = request.password();

        User user = findUserById(userId);

        validateOriginalPassword(user, originalPassword);
        validateNewPasswordIsDifferent(originalPassword, newPassword);

        user.updatePassword(newPassword, passwordEncoder);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXISTING));
    }

    private void verifyUniqueNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new EntityExistsException(NICKNAME_DUPLICATE);
        }
    }

    private void verifyUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException(EMAIL_DUPLICATE);
        }
    }

    private void validateOriginalPassword(User user, String originalPassword) {
        if (!user.checkPassword(originalPassword, passwordEncoder)) {
            throw new AccessDeniedException(PASSWORD_NOT_MATCH);
        }
    }

    private void validateNewPasswordIsDifferent(String originalPassword, String newPassword) {
        if (originalPassword.equals(newPassword)) {
            throw new IllegalArgumentException(PASSWORD_IS_SAME_AS_BEFORE);
        }
    }

}
