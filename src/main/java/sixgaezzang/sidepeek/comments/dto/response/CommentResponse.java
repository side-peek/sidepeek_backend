package sixgaezzang.sidepeek.comments.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Schema(description = "댓글 정보")
@Builder
public record CommentResponse(
    @Schema(description = "댓글 식별자", example = "1")
    Long id,

    @Schema(description = "댓글 작성자 정보(익명 댓글은 null)")
    UserSummary user,

    @Schema(description = "댓글 작성자와 로그인 사용자 일치 여부", example = "true")
    boolean isOwner,

    @Schema(description = "익명 댓글 여부", example = "true")
    boolean isAnonymous,

    @Schema(description = "댓글 내용", example = "우와 이 프로젝트 대박인데요?")
    String content,

    @Schema(description = "댓글 생성 시각", example = "2024-03-02 20:17:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    @Schema(description = "대댓글 목록")
    List<ReplyResponse> replies
) {

    public static CommentResponse from(Comment comment, boolean isOwner,
        List<ReplyResponse> replies) {
        User user = comment.getUser();
        UserSummary userSummary =
            comment.isAnonymous() ? null : UserSummary.from(comment.getUser());

        return CommentResponse.builder()
            .id(comment.getId())
            .user(userSummary)
            .isOwner(isOwner)
            .isAnonymous(comment.isAnonymous())
            .content(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .replies(replies)
            .build();
    }

}
