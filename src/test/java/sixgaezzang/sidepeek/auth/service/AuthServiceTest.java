package sixgaezzang.sidepeek.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.auth.domain.RefreshToken;
import sixgaezzang.sidepeek.auth.dto.request.LoginRequest;
import sixgaezzang.sidepeek.auth.dto.request.ReissueTokenRequest;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;
import sixgaezzang.sidepeek.auth.jwt.JWTManager;
import sixgaezzang.sidepeek.common.exception.TokenValidationFailException;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;
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

    @Autowired
    JWTManager jwtManager;

    @MockBean
    RefreshTokenService refreshTokenService;

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
            LoginRequest loginRequest = new LoginRequest(email, password);

            // when
            LoginResponse response = authService.login(loginRequest);

            // then
            assertThat(response.accessToken()).isNotNull();
            assertThat(response.refreshToken()).isNotNull();
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

            // when
            UserSummary response = authService.loadUser(user.getId());

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

    @Nested
    class 토큰_재발급_테스트 {

        Long expiredAt;

        @BeforeEach
        void setUp() {
            expiredAt = getExpiredAt(1);
        }

        @Test
        void 유효한_리프레시_토큰인_경우_재발급에_성공한다() {
            // given
            User user = createUser();
            String refreshToken = jwtManager.generateRefreshToken(user.getId());
            ReissueTokenRequest request = new ReissueTokenRequest(refreshToken);
            RefreshToken redisRefreshToken = new RefreshToken(user.getId(), refreshToken,
                expiredAt);

            given(refreshTokenService.getById(anyLong())).willReturn(redisRefreshToken);

            // when
            LoginResponse response = authService.reissue(request);

            // then
            assertThat(response.accessToken()).isNotNull();
            assertThat(response.refreshToken()).isNotNull();
            assertThat(response.user()).extracting("id", "nickname", "profileImageUrl")
                .containsExactly(user.getId(), user.getNickname(), user.getProfileImageUrl());
        }

        @Test
        void 유효하지_않은_리프레시_토큰인_경우_재발급에_실패한다() {
            // given
            String invalidToken = faker.internet().uuid();
            ReissueTokenRequest request = new ReissueTokenRequest(invalidToken);

            // when
            ThrowingCallable reissue = () -> authService.reissue(request);

            // then
            assertThatExceptionOfType(TokenValidationFailException.class).isThrownBy(reissue)
                .withMessage("유효하지 않은 토큰입니다.");
        }

        // TODO: 만료된 리프레시 토큰일 경우 재발급에 실패한다.

        // TODO: 요청된_리프레시_토큰과_저장된_토큰이_다를_경우_재발급에_실패한다.

        @Test
        void 존재하지_않는_사용자인_경우_재발급에_실패한다() {
            // given
            Long invalidUserId = faker.random().nextLong(Long.MAX_VALUE);
            String refreshToken = jwtManager.generateRefreshToken(invalidUserId);
            RefreshToken redisRefreshToken = new RefreshToken(invalidUserId, refreshToken,
                expiredAt);
            ReissueTokenRequest request = new ReissueTokenRequest(refreshToken);

            given(refreshTokenService.getById(anyLong())).willReturn(redisRefreshToken);

            // when
            ThrowingCallable reissue = () -> authService.reissue(request);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(reissue)
                .withMessage("존재하지 않는 사용자입니다.");
        }
    }

    private User createUser() {
        User user = User.builder()
            .email(email)
            .password(new Password(password, passwordEncoder))
            .nickname(nickname)
            .build();

        return userRepository.save(user);
    }

    private Long getExpiredAt(int expiredAfter) {
        Instant now = Instant.now();
        Instant expiredAt = now.plusMillis(expiredAfter);

        return expiredAt.getEpochSecond();
    }

}
