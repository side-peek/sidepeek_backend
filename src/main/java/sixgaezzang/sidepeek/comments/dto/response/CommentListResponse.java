package sixgaezzang.sidepeek.comments.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "댓글 목록 응답 정보")
public record CommentListResponse(
    @Schema(description = "댓글 목록 정보(오래된 순)")
    List<CommentResponse> comments
) {
}
