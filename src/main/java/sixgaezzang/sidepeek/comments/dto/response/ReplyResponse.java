package sixgaezzang.sidepeek.comments.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Schema(description = "대댓글 응답 정보")
@Builder
public record ReplyResponse(
    @Schema(description = "댓글 식별자", example = "3")
    Long id,

    @Schema(description = "상위 댓글 식별자(상위 댓글이라면 null)", example = "1")
    Long parentId,

    @Schema(description = "대댓글 작성자 정보(익명 댓글은 null)")
    UserSummary user,

    @Schema(description = "대댓글 작성자와 로그인 사용자 일치 여부")
    boolean isOwner,

    @Schema(description = "익명 대댓글 여부")
    boolean isAnonymous,

    @Schema(description = "댓글 내용", example = "좋게 봐주셔서 감사합니다!")
    String content,

    @Schema(description = "대댓글 생성 시각", example = "2024-03-05 20:17:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

    public static ReplyResponse from(Comment comment, boolean isOwner) {
        User user = comment.getUser();
        UserSummary userSummary =
            comment.isAnonymous() ? null : UserSummary.from(comment.getUser());

        return ReplyResponse.builder()
            .id(comment.getId())
            .parentId(comment.getParent().getId())
            .user(userSummary)
            .isOwner(isOwner)
            .isAnonymous(comment.isAnonymous())
            .content(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .build();
    }

}
