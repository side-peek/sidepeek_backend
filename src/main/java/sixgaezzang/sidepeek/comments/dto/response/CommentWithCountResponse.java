package sixgaezzang.sidepeek.comments.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record CommentWithCountResponse(
    List<CommentResponse> results,
    Long commentCount
) {

    public static CommentWithCountResponse from(List<CommentResponse> comments, Long commentCount) {
        return CommentWithCountResponse.builder()
            .results(comments)
            .commentCount(commentCount)
            .build();
    }

}
