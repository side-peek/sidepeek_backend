package sixgaezzang.sidepeek.like.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import sixgaezzang.sidepeek.like.domain.Like;

@Schema(description = "좋아요 정보(결과)")
@Builder
public record LikeResponse(
    @Schema(description = "좋아요 식별자", example = "1")
    Long id,

    @Schema(description = "좋아요한 프로젝트 식별자", example = "1")
    Long projectId,

    @Schema(description = "좋아요한 사용자 식별자", example = "3")
    Long userId,

    @Schema(description = "좋아요 생성 시각", example = "2024-03-02 20:17:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

    public static LikeResponse from(Like like) {
        return LikeResponse.builder()
            .id(like.getId())
            .projectId(like.getProject().getId())
            .userId(like.getUser().getId())
            .createdAt(like.getCreatedAt())
            .build();
    }
}
