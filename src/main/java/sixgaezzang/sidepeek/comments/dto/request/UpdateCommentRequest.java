package sixgaezzang.sidepeek.comments.dto.request;

import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CONTENT_IS_NULL;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CONTENT_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.IS_ANONYMOUS_IS_NULL;
import static sixgaezzang.sidepeek.comments.util.CommentConstant.MAX_CONTENT_LENGTH;
import static sixgaezzang.sidepeek.common.doc.description.CommentDescription.COMMENT_CONTENT_DESCRIPTION;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "댓글/대댓글 수정 요청 정보")
public record UpdateCommentRequest(
    @Schema(description = "익명 댓글 여부", example = "false")
    @NotNull(message = IS_ANONYMOUS_IS_NULL)
    Boolean isAnonymous,

    @Schema(description = COMMENT_CONTENT_DESCRIPTION, example = "우와 이 프로젝트 대박인데요?")
    @Size(max = MAX_CONTENT_LENGTH, message = CONTENT_OVER_MAX_LENGTH)
    @NotBlank(message = CONTENT_IS_NULL)
    String content
) {

}
