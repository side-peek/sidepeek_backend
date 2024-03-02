package sixgaezzang.sidepeek.comments.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Schema(description = "댓글 응답 정보")
public record CommentResponse(
    @Schema(description = "댓글 식별자", example = "1")
    Long id,

    @Schema(description = "댓글 작성자 정보(익명 댓글은 null)")
    UserSummary owner,

    @Schema(description = "댓글 내용", example = "우와 이 프로젝트 대박인데요?")
    String content,

    @Schema(description = "댓글 생성 시각", example = "2024-03-02 20:17:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {
}
