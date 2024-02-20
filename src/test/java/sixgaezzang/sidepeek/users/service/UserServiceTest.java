package sixgaezzang.sidepeek.users.service;

import static io.micrometer.common.util.StringUtils.isBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import jakarta.persistence.EntityExistsException;
import java.util.List;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.Provider;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.request.SignUpRequest;
import sixgaezzang.sidepeek.users.dto.response.CheckDuplicateResponse;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;
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
    class 회원_닉네임_검색_테스트 {

        static final int USER_COUNT = 5;
        static final String[] users = {"zzang1", "zzang2", "zzang3", "coco1", "coco2"};

        @BeforeEach
        void setUp() {
            for (int i = 0; i < USER_COUNT; i++) {
                User user = createUser(users[i] + "@google.com", password, users[i]);
                userRepository.save(user);
            }
        }

        @ParameterizedTest(name = "[{index}] {0}으로 검색할 때 " + USER_COUNT + "명의 모든 회원이 나온다.")
        @NullAndEmptySource
        void 검색어_없이_전체_회원_닉네임_검색에_성공한다(String keyword) {
            // given, when
            List<UserSummary> users = userService.searchByNickname(keyword)
                .users();

            // then
            assertThat(users.size()).isEqualTo(USER_COUNT);
        }

        @ParameterizedTest(name = "[{index}] {0}으로 검색할 때 {1}명의 회원이 나온다.")
        @CsvSource(value = {"zzang:3", "coco:2"}, delimiter = ':')
        void 검색어로_회원_닉네임_검색에_성공한다(String keyword, int count) {
            // given, when
            List<UserSummary> users = userService.searchByNickname(keyword)
                .users();

            // then
            assertThat(users.size()).isEqualTo(count);
        }

        @Test
        void 검색어_최대_글자_수가_넘어_회원_닉네임_검색에_실패한다() {
            // given
            int keywordLength = MAX_NICKNAME_LENGTH + 1;
            String keyword = "a".repeat(keywordLength);

            // when
            ThrowingCallable search = () -> userService.searchByNickname(keyword);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(search)
                .withMessage("최대 " + MAX_NICKNAME_LENGTH + "자의 키워드로 검색할 수 있습니다.");
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

    @Nested
    class 닉네임_중복_확인_테스트 {

        @Test
        void 닉네임이_중복되지_않은_경우_중복_확인에_성공한다() {
            // when
            CheckDuplicateResponse response = userService.checkNicknameDuplicate(nickname);

            // then
            assertThat(response.isDuplicated()).isFalse();
        }

        @Test
        void 닉네임이_중복된_경우_중복_확인에_성공한다() {
            // given
            String duplicatedNickname = nickname;
            User user = createUser(email, password, duplicatedNickname);
            userRepository.save(user);

            // when
            CheckDuplicateResponse response = userService.checkNicknameDuplicate(
                duplicatedNickname);

            // then
            assertThat(response.isDuplicated()).isTrue();
        }

        @Test
        void 닉네임이_최대_길이를_초과하는_경우_중복_확인에_실패한다() {
            // given
            String longNickname = faker.lorem()
                .characters(User.MAX_NICKNAME_LENGTH + 1);

            // when
            ThrowingCallable checkNicknameDuplicate = () -> userService.checkNicknameDuplicate(
                longNickname);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                    checkNicknameDuplicate)
                .withMessage("닉네임은 " + User.MAX_NICKNAME_LENGTH + "자 이하여야 합니다.");
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
