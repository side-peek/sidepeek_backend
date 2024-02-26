package sixgaezzang.sidepeek.auth.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("refreshToken")
@Builder
public record RefreshToken(
    @Id
    Long id,
    String refreshToken,
    @TimeToLive
    Long expiredAt
) {

    public static final long MILLISECONDS_PER_SECOND = 1000L;

    public static RefreshToken from(Long userId, String refreshToken, Long expiredAt) {
        return RefreshToken.builder()
            .id(userId)
            .refreshToken(refreshToken)
            .expiredAt(expiredAt / MILLISECONDS_PER_SECOND)
            .build();
    }

}
