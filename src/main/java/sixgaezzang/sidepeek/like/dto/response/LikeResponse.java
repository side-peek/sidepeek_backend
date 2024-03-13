package sixgaezzang.sidepeek.like.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "좋아요 정보(결과)")
public record LikeResponse(
    @Schema(description = "좋아요 결과")
    boolean isLiked
) {

    public static LikeResponse from(boolean isLiked) {
        return new LikeResponse(isLiked);
    }
}
