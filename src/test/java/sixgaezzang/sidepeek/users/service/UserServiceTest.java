package sixgaezzang.sidepeek.users.service;

import static io.micrometer.common.util.StringUtils.isBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import jakarta.persistence.EntityExistsException;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.Provider;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.request.SignUpRequest;
import sixgaezzang.sidepeek.users.dto.response.CheckDuplicateResponse;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(ReplaceUnderscores.class)
class UserServiceTest {

    static final Faker faker = new Faker();

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    String email;
    String password;
    String nickname;

    @BeforeEach
    void setUp() {
        email = faker.internet().emailAddress();
        password = faker.internet().password(8, 100, true, true, true);
        nickname = faker.internet().username();
    }

    @Nested
    class 회원가입_테스트 {

        @Test
        void 회원가입에_성공한다() {
            // given
            SignUpRequest request = new SignUpRequest(email, password, nickname);

            // when
            Long saved = userService.signUp(request, Provider.BASIC);

            // then
            User actual = userRepository.findById(saved).get();
            Password encodedPassword = actual.getPassword();
            assertThat(actual).extracting("email", "nickname", "provider")
                .containsExactly(email, nickname, Provider.BASIC);
            assertThat(encodedPassword.check(password, passwordEncoder)).isTrue();
        }

        @Test
        void 이메일이_중복된_경우_회원가입에_실패한다() {
            // given
            String duplicatedEmail = email;
            User user = createUser(duplicatedEmail, password, nickname);
            userRepository.save(user);

            String newNickname = faker.internet().username();
            SignUpRequest request = new SignUpRequest(duplicatedEmail, password, newNickname);

            // when
            ThrowingCallable signup = () -> userService.signUp(request, Provider.BASIC);

            // then
            assertThatExceptionOfType(EntityExistsException.class).isThrownBy(signup)
                .withMessage("이미 사용 중인 이메일입니다.");
        }

        @Test
        void 닉네임이_중복된_경우_회원가입에_실패한다() {
            // given
            String duplicatedNickname = nickname;
            User user = createUser(email, password, duplicatedNickname);
            userRepository.save(user);

            String newEmail = faker.internet().emailAddress();
            SignUpRequest request = new SignUpRequest(newEmail, password, duplicatedNickname);

            // when
            ThrowingCallable signup = () -> userService.signUp(request, Provider.BASIC);

            // then
            assertThatExceptionOfType(EntityExistsException.class).isThrownBy(signup)
                .withMessage("이미 사용 중인 닉네임입니다.");
        }

        @Test
        void 이메일_형식이_올바르지_않은_경우_회원가입에_실패한다() {
            // given
            String invalidEmail = "invalid-email";
            SignUpRequest request = new SignUpRequest(invalidEmail, password, nickname);

            // when
            ThrowingCallable signup = () -> userService.signUp(request, Provider.BASIC);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(signup)
                .withMessage("이메일 형식이 올바르지 않습니다.");
        }

        @Test
        void 비밀번호_형식이_올바르지_않은_경우_회원가입에_실패한다() {
            // given
            String invalidPassword = "invalid-password";
            SignUpRequest request = new SignUpRequest(email, invalidPassword, nickname);

            // when
            ThrowingCallable signup = () -> userService.signUp(request, Provider.BASIC);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(signup)
                .withMessage("비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.");
        }
    }

    @Nested
    class 이메일_중복_확인_테스트 {

        @Test
        void 이메일이_중복되지_않은_경우_중복_확인에_성공한다() {
            // when
            CheckDuplicateResponse response = userService.checkEmailDuplicate(email);

            // then
            assertThat(response.isDuplicated()).isFalse();
        }

        @Test
        void 이메일이_중복된_경우_중복_확인에_성공한다() {
            // given
            String duplicatedEmail = email;
            User user = createUser(duplicatedEmail, password, nickname);
            userRepository.save(user);

            // when
            CheckDuplicateResponse response = userService.checkEmailDuplicate(duplicatedEmail);

            // then
            assertThat(response.isDuplicated()).isTrue();
        }

        @Test
        void 이메일_형식이_올바르지_않은_경우_중복_확인에_실패한다() {
            // given
            String invalidEmail = "invalid-email";

            // when
            ThrowingCallable checkEmailDuplicate = () -> userService.checkEmailDuplicate(
                invalidEmail);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                    checkEmailDuplicate)
                .withMessage("이메일 형식이 올바르지 않습니다.");
        }
    }

    private User createUser(String email, String password, String nickname) {
        return User.builder()
            .email(isBlank(email) ? this.email : email)
            .password(isBlank(password) ? new Password(this.password, passwordEncoder)
                : new Password(password, passwordEncoder))
            .nickname(isBlank(nickname) ? this.nickname : nickname)
            .provider(Provider.BASIC)
            .build();
    }
}
