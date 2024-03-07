package sixgaezzang.sidepeek.users.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.EXCESSIVE_NICKNAME_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import jakarta.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.util.FakeEntityProvider;
import sixgaezzang.sidepeek.projects.util.FakeValueProvider;
import sixgaezzang.sidepeek.users.domain.Password;
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

    static final int USER_COUNT = 5;
    List<String> userNicknames;

    @BeforeEach
    void setUp() {
        email = FakeValueProvider.createEmail();
        password = FakeValueProvider.createPassword();
        nickname = FakeValueProvider.createNickname();

        userRepository.deleteAll(); // TODO: 아래 TODO 참고!
        userNicknames = new ArrayList<>();
        for (int i = 0; i < USER_COUNT; i++) {
            userNicknames.add(createAndSaveUser().getNickname());
        }
    }

    private User createAndSaveUser() {
        User newUser = FakeEntityProvider.createUser();
        return userRepository.save(newUser);
    }

    @Nested
    class 회원_닉네임_검색_테스트 {

        @ParameterizedTest(name = "[{index}] {0}으로 검색할 때 " + USER_COUNT + "명의 모든 회원이 나온다.")
        @NullAndEmptySource
        void 검색어_없이_전체_회원_닉네임_검색에_성공한다(String keyword) {
            // given, when
            List<UserSummary> users = userService.searchByNickname(keyword)
                .users();

            // then
            // TODO: 분명 5개를 BeforeEach로 저장했는데 그 이상이 나온다.
            assertThat(users).hasSize(USER_COUNT);
        }

        @Test
        void 검색어로_회원_닉네임_검색에_성공한다() {
            // given,
            String keyword = FakeValueProvider.createEnglishKeyword();
            int count = 0;
            for (String nickname : userNicknames) {
                if (nickname.contains(keyword)) {
                    count++;
                }
            }

            // when
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
                .withMessage(EXCESSIVE_NICKNAME_LENGTH.getMessage());
        }

    }

    @Nested
    class 회원가입_테스트 {

        @Test
        void 회원가입에_성공한다() {
            // given
            SignUpRequest request = new SignUpRequest(email, password, nickname);

            // when
            Long saved = userService.signUp(request);

            // then
            User actual = userRepository.findById(saved).get();
            Password encodedPassword = actual.getPassword();
            assertThat(actual).extracting("email", "nickname")
                .containsExactly(email, nickname);
            assertThat(encodedPassword.check(password, passwordEncoder)).isTrue();
        }

        @Test
        void 이메일이_중복된_경우_회원가입에_실패한다() {
            // given
            String duplicatedEmail = email;
            User user = FakeEntityProvider.createUser(duplicatedEmail, password, nickname, passwordEncoder);
            userRepository.save(user);

            String newNickname = faker.internet().username();
            SignUpRequest request = new SignUpRequest(duplicatedEmail, password, newNickname);

            // when
            ThrowingCallable signup = () -> userService.signUp(request);

            // then
            assertThatExceptionOfType(EntityExistsException.class).isThrownBy(signup)
                .withMessage("이미 사용 중인 이메일입니다.");
        }

        @Test
        void 닉네임이_중복된_경우_회원가입에_실패한다() {
            // given
            String duplicatedNickname = nickname;
            User user = FakeEntityProvider.createUser(email, password, duplicatedNickname, passwordEncoder);
            userRepository.save(user);

            String newEmail = FakeValueProvider.createEmail();
            SignUpRequest request = new SignUpRequest(newEmail, password, duplicatedNickname);

            // when
            ThrowingCallable signup = () -> userService.signUp(request);

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
            ThrowingCallable signup = () -> userService.signUp(request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(signup)
                .withMessage("유효하지 않은 이메일 형식입니다.");
        }

        @Test
        void 비밀번호_형식이_올바르지_않은_경우_회원가입에_실패한다() {
            // given
            String invalidPassword = "invalid-password";
            SignUpRequest request = new SignUpRequest(email, invalidPassword, nickname);

            // when
            ThrowingCallable signup = () -> userService.signUp(request);

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
            User user = FakeEntityProvider.createUser(duplicatedEmail, password, nickname, passwordEncoder);
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
                .withMessage("유효하지 않은 이메일 형식입니다.");
        }
    }

    @Nested
    class 회원_프로필_조회_테스트 {

        @Test
        void 수정_이력이_없는_회원_프로필_조회에_성공한다() {
            // given

            // when

            // then

        }

        @Test
        void 수정_이력이_있는_회원_프로필_조회에_성공한다() {
            // given

            // when

            // then

        }

        @Test
        void 존재하는_회원_프로필_조회에_성공한다() {
            // given

            // when

            // then
        }

        @Test
        void 존재하지_않는_회원_프로필_조회에_실패한다() {
            // given

            // when

            // then
        }

        @ParameterizedTest
        @NullSource
        void 프로필_회원_Id가_유효하지_않아_회원_프로필_조회에_실패한다(Long id) {
            // given

            // when

            // then
        }

    }

    @Nested
    class 회원_프로필_수정_테스트 {

        @Test
        void 로그인_Id가_회원_Id와_일치하여_회원_프로필_수정에_성공한다() {
            // given

            // when

            // then

        }

        @Test
        void 로그인을_하지_않아_회원_프로필_수정에_실패한다() {
            // given

            // when

            // then

        }

        @Test
        void 로그인_Id와_회원_Id가_일치하지_않아_회원_프로필_수정에_실패한다() {
            // given

            // when

            // then

        }

        @Test
        void 존재하지_않는_회원_프로필_수정에_실패한다() {
            // given

            // when

            // then

        }

        @Test
        void 유효하지_않은_프로필_정보로_프로필_수정에_실패한다() {
            // given

            // when

            // then

        }

    }

    @Nested
    class 회원_비밀번호_수정_테스트 {

        // TODO: 회원_비밀번호_수정_테스트 작성 필요

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
            User user = FakeEntityProvider.createUser(email, password, duplicatedNickname, passwordEncoder);
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
                .characters(MAX_NICKNAME_LENGTH + 1);

            // when
            ThrowingCallable checkNicknameDuplicate = () -> userService.checkNicknameDuplicate(
                longNickname);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                    checkNicknameDuplicate)
                .withMessage(NICKNAME_OVER_MAX_LENGTH);
        }
    }
}
