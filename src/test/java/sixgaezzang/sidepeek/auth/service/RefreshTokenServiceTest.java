package sixgaezzang.sidepeek.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static sixgaezzang.sidepeek.auth.domain.RefreshToken.MILLISECONDS_PER_SECOND;

import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.auth.domain.RefreshToken;
import sixgaezzang.sidepeek.auth.jwt.JWTManager;
import sixgaezzang.sidepeek.common.exception.TokenValidationFailException;

@SpringBootTest
@Transactional
@DisplayNameGeneration(ReplaceUnderscores.class)
class RefreshTokenServiceTest {

    static final Faker faker = new Faker();

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JWTManager jwtManager;

    String refreshToken;

    @BeforeEach
    void setUp() {
        Long userId = faker.random().nextLong(Long.MAX_VALUE);
        refreshToken = jwtManager.generateRefreshToken(userId);
    }

    @Nested
    class 리프레시_토큰_저장_테스트 {

        @Test
        void 토큰_저장에_성공한다() {
            // when
            RefreshToken actual = refreshTokenService.save(refreshToken);

            // then
            assertThat(actual).isNotNull();
            assertThat(actual.refreshToken()).isEqualTo(refreshToken);
            assertThat(actual.expiredAt()).isGreaterThan(
                System.currentTimeMillis() / MILLISECONDS_PER_SECOND);
        }

        @Test
        void 유효하지_않은_토큰인_경우_저장에_실패한다() {
            // given
            String invalidToken = faker.animal().name();

            // when
            ThrowingCallable saven = () -> refreshTokenService.save(invalidToken);

            // then
            assertThatThrownBy(saven).isInstanceOf(TokenValidationFailException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
        }
    }
}
