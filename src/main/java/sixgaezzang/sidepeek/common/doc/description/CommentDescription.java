package sixgaezzang.sidepeek.common.doc.description;

import static sixgaezzang.sidepeek.comments.util.CommentConstant.MAX_CONTENT_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentDescription {
    // SaveCommentRequest, UpdateCommentRequest
    public static final String COMMENT_OWNER_ID_DESCRIPTION = "댓글 작성자 식별자";
    public static final String COMMENT_PROJECT_ID_DESCRIPTION = "프로젝트 식별자(댓글인 경우 필수)";
    public static final String COMMENT_PARENT_ID_DESCRIPTION = "부모 댓글 식별자(대댓글인 경우 필수)";
    public static final String IS_ANONYMOUS_DESCRIPTION = "익명 댓글 여부";
    public static final String COMMENT_CONTENT_DESCRIPTION = "댓글 내용, " + MAX_CONTENT_LENGTH + "자 이하";
}
