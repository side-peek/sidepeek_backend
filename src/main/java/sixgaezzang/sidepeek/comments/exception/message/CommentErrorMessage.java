package sixgaezzang.sidepeek.comments.exception.message;

import static sixgaezzang.sidepeek.comments.util.CommentConstant.MAX_CONTENT_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentErrorMessage {

    // Common
    public static final String COMMENT_NOT_EXISTING = "존재하지 않는 댓글 입니다.";
    public static final String PARENT_COMMENT_NOT_EXISTING = "존재하지 않는 부모 댓글 입니다.";

    // isAnonymous
    public static final String IS_ANONYMOUS_IS_NULL = "익명 댓글 여부를 입력해주세요.";

    // content
    public static final String CONTENT_IS_NULL = "댓글 내용을 입력해주세요.";
    public static final String CONTENT_OVER_MAX_LENGTH = "댓글은 " + MAX_CONTENT_LENGTH + "자 이하여야 합니다.";

}
