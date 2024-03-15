package sixgaezzang.sidepeek.comments.dto.request;

import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CONTENT_IS_NULL;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CONTENT_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.IS_ANONYMOUS_IS_NULL;
import static sixgaezzang.sidepeek.comments.util.CommentConstant.MAX_CONTENT_LENGTH;
import static sixgaezzang.sidepeek.common.doc.description.CommentDescription.COMMENT_CONTENT_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.CommentDescription.COMMENT_OWNER_ID_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.CommentDescription.COMMENT_PARENT_ID_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.CommentDescription.COMMENT_PROJECT_ID_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.CommentDescription.IS_ANONYMOUS_DESCRIPTION;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OWNER_ID_IS_NULL;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.users.domain.User;

@Schema(description = "댓글/대댓글 생성 요청 정보")
public record SaveCommentRequest(
    @Schema(description = COMMENT_OWNER_ID_DESCRIPTION, example = "1")
    @NotNull(message = OWNER_ID_IS_NULL)
    @Min(value = MIN_ID, message = "작성자 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    Long ownerId,

    @Schema(description = COMMENT_PROJECT_ID_DESCRIPTION, example = "1")
    @Min(value = MIN_ID, message = "프로젝트 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    Long projectId,

    @Schema(description = COMMENT_PARENT_ID_DESCRIPTION)
    @Min(value = MIN_ID, message = "댓글 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    Long parentId,

    @Schema(description = IS_ANONYMOUS_DESCRIPTION, example = "false")
    @NotNull(message = IS_ANONYMOUS_IS_NULL)
    Boolean isAnonymous,

    @Schema(description = COMMENT_CONTENT_DESCRIPTION, example = "우와 이 프로젝트 대박인데요?")
    @Size(max = MAX_CONTENT_LENGTH, message = CONTENT_OVER_MAX_LENGTH)
    @NotBlank(message = CONTENT_IS_NULL)
    String content
) {

    public Comment toEntity(Project project, Comment parent, User user) {
        return Comment.builder()
            .project(project)
            .parent(parent)
            .user(user)
            .isAnonymous(this.isAnonymous)
            .content(this.content)
            .build();
    }

}
