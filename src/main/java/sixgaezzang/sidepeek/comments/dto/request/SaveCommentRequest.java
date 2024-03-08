package sixgaezzang.sidepeek.comments.dto.request;

import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CONTENT_IS_NULL;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CONTENT_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.IS_ANONYMOUS_IS_NULL;
import static sixgaezzang.sidepeek.comments.util.CommentConstant.MAX_COMMENT_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OWNER_ID_IS_NULL;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "댓글/대댓글 생성/수정 요청 정보")
public record SaveCommentRequest(
    @Schema(description = "댓글 작성자 식별자", example = "1")
    @NotNull(message = OWNER_ID_IS_NULL)
    @Min(value = MIN_ID, message = "작성자 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    Long ownerId,

    @Schema(description = "프로젝트 식별자", example = "1")
    @Min(value = MIN_ID, message = "프로젝트 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    Long projectId,

    @Schema(description = "부모 댓글 식별자", example = "null")
    @Min(value = MIN_ID, message = "댓글 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    Long parentId,

    @Schema(description = "익명 댓글 여부", example = "false")
    @NotNull(message = IS_ANONYMOUS_IS_NULL)
    Boolean isAnonymous,

    @Schema(description = "댓글 내용", example = "우와 이 프로젝트 대박인데요?")
    @Size(max = MAX_COMMENT_LENGTH, message = CONTENT_OVER_MAX_LENGTH)
    @NotBlank(message = CONTENT_IS_NULL)
    String content
) {
}
