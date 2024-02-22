package sixgaezzang.sidepeek.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import jakarta.persistence.EntityNotFoundException;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.auth.dto.request.LoginRequest;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummaryResponse;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(ReplaceUnderscores.class)
class AuthServiceTest {

    static final Faker faker = new Faker();

    @Autowired
    AuthService authService;

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
    class 로그인_테스트 {

        @Test
        void 로그인에_성공한다() {
            // given
            User user = createUser();
            userRepository.save(user);
            LoginRequest loginRequest = new LoginRequest(email, password);

            // when
            LoginResponse response = authService.login(loginRequest);

            // then
            assertThat(response.accessToken()).isNotNull();
            assertThat(response.user()).extracting("id", "nickname", "profileImageUrl")
                .containsExactly(user.getId(), user.getNickname(), user.getProfileImageUrl());
        }

        @Test
        void 존재하지_않는_사용자_이메일인_경우_로그인에_실패한다() {
            // given
            LoginRequest loginRequest = new LoginRequest(email, password);

            // when
            ThrowingCallable login = () -> authService.login(loginRequest);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(login)
                .withMessage("존재하지 않는 사용자입니다.");
        }

        @Test
        void 비밀번호가_일치하지_않는_경우_로그인에_실패한다() {
            // given
            User user = createUser();
            userRepository.save(user);
            String unmatchedPassword = faker.internet().password(8, 100, true, true, true);
            LoginRequest loginRequest = new LoginRequest(email, unmatchedPassword);

            // when
            ThrowingCallable login = () -> authService.login(loginRequest);

            // then
            assertThatExceptionOfType(BadCredentialsException.class).isThrownBy(login)
                .withMessage("비밀번호가 일치하지 않습니다.");
        }
    }

    @Nested
    class 사용자_조회_테스트 {

        @Test
        void 사용자_조회에_성공한다() {
            // given
            User user = createUser();
            userRepository.save(user);

            // when
            UserSummaryResponse response = authService.loadUser(user.getId());

            // then
            assertThat(response).extracting("id", "nickname", "profileImageUrl")
                .containsExactly(user.getId(), user.getNickname(), user.getProfileImageUrl());
        }

        @Test
        void 존재하지_않는_사용자인_경우_사용자_조회에_실패한다() {
            // given
            Long invalidId = faker.random().nextLong(Long.MAX_VALUE);

            // when
            ThrowingCallable loadUser = () -> authService.loadUser(invalidId);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(loadUser)
                .withMessage("존재하지 않는 사용자입니다.");
        }
    }

    private User createUser() {
        return User.builder()
            .email(email)
            .password(new Password(password, passwordEncoder))
            .nickname(nickname)
            .build();
    }

}
