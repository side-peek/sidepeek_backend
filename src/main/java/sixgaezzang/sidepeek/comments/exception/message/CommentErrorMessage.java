package sixgaezzang.sidepeek.comments.exception.message;

import static sixgaezzang.sidepeek.comments.util.CommentConstant.MAX_COMMENT_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentErrorMessage {
    // isAnonymous
    public static final String IS_ANONYMOUS_IS_NULL = "익명 댓글 여부를 입력해주세요.";

    // content
    public static final String CONTENT_IS_NULL = "댓글 내용을 입력해주세요.";
    public static final String CONTENT_OVER_MAX_LENGTH = "댓글은 " + MAX_COMMENT_LENGTH + "자 이하여야 합니다.";
}
