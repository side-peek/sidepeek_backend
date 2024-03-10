package sixgaezzang.sidepeek.comments.exception.message;

import static sixgaezzang.sidepeek.comments.util.CommentConstant.MAX_CONTENT_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentErrorMessage {

    // Common
    public static final String COMMENT_ID_IS_NULL = "댓글 Id를 입력해주세요.";
    public static final String COMMENT_NOT_EXISTING = "존재하지 않는 댓글 입니다.";
    public static final String PARENT_COMMENT_NOT_EXISTING = "존재하지 않는 부모 댓글 입니다.";
    public static final String CHILD_COMMENT_CANNOT_BE_PARENT = "대댓글의 댓글은 작성할 수 없습니다.";
    public static final String PROJECT_ID_AND_PARENT_ID_IS_NULL = "프로젝트 Id나 부모 댓글 Id를 입력해주세요.";

    // isAnonymous
    public static final String IS_ANONYMOUS_IS_NULL = "익명 댓글 여부를 입력해주세요.";

    // content
    public static final String CONTENT_IS_NULL = "댓글 내용을 입력해주세요.";
    public static final String CONTENT_OVER_MAX_LENGTH = "댓글은 " + MAX_CONTENT_LENGTH + "자 이하여야 합니다.";

}
