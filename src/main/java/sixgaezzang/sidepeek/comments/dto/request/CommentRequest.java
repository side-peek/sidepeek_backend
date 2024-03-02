package sixgaezzang.sidepeek.comments.dto.request;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "댓글 생성/수정 요청 정보")
public record CommentRequest(
    @Schema(description = "댓글 작성자 식별자", example = "1")
    @Min(value = MIN_ID, message = "")
    Long ownerId,

    @Schema(description = "익명 댓글 여부", example = "false")
    @NotNull
    Boolean isAnonymous,

    @Schema(description = "댓글 내용", example = "우와 이 프로젝트 대박인데요?")
    @Size(max = MAX_TEXT_LENGTH, message = "")
    @NotBlank
    String content
) {
}
