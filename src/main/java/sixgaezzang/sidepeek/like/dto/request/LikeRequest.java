package sixgaezzang.sidepeek.like.dto.request;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

// TODO : 로그인한 사용자로 좋아요할 수 있는데, Long userId가 필요할까?
public record LikeRequest(
    // project id
    @Schema(description = "프로젝트 식별자(댓글인 경우 필수)", example = "1")
    @Min(value = MIN_ID, message = "프로젝트 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    Long projectId
) {

}
