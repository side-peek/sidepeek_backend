package sixgaezzang.sidepeek.projects.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import sixgaezzang.sidepeek.projects.dto.response.ProjectBannerResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class PopularProjectCacheService {

    private static final String WEEKLY_POPULAR_CACHE_KEY = "popularProjectsLastWeek";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    // 캐시에서 인기 프로젝트를 조회
    public Optional<ProjectBannerResponse> getPopularProjects() {
        String value = redisTemplate.opsForValue().get(WEEKLY_POPULAR_CACHE_KEY);

        if (isBlank(value)) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(value.trim(), ProjectBannerResponse.class));
        } catch (Exception e) {
            throw new RuntimeException("캐시에서 데이터를 가져오는데 실패하였습니다.");
        }
    }

    // 캐시에 인기 프로젝트를 저장하고 TTL 설정
    public void putPopularProjects(ProjectBannerResponse data, LocalDateTime expireTime) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        try {
            String value = objectMapper.writeValueAsString(data);
            valueOps.set(WEEKLY_POPULAR_CACHE_KEY, value, calculateTTL(expireTime));
        } catch (Exception e) {
            throw new RuntimeException("캐시에 데이터를 저장하는데 실패하였습니다.");
        }
    }

    private long calculateTTL(LocalDateTime expireTime) {
        LocalDateTime now = LocalDateTime.now(); // 현재 시각

        // 현재 시각과 lastDayOfWeek 자정 사이의 차이를 계산
        Duration duration = Duration.between(now, expireTime);

        return duration.getSeconds();
    }
}
