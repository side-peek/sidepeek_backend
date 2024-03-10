package sixgaezzang.sidepeek.comments.util.validation;

import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CHILD_COMMENT_CANNOT_BE_PARENT;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.COMMENT_ID_IS_NULL;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CONTENT_IS_NULL;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CONTENT_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.IS_ANONYMOUS_IS_NULL;
import static sixgaezzang.sidepeek.comments.util.CommentConstant.MAX_CONTENT_LENGTH;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateNotBlank;

import io.jsonwebtoken.lang.Assert;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.comments.domain.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentValidator {

    // Common
    public static void validateCommentId(Long commentId) {
        Assert.notNull(commentId, COMMENT_ID_IS_NULL);
    }

    public static void validateParentCommentHasParent(Comment parent) {
        if (Objects.nonNull(parent.getParent())) {
            throw new IllegalArgumentException(CHILD_COMMENT_CANNOT_BE_PARENT);
        }
    }

    // Content
    public static void validateCommentContent(String content) {
        validateNotBlank(content, CONTENT_IS_NULL);
        validateMaxLength(content, MAX_CONTENT_LENGTH, CONTENT_OVER_MAX_LENGTH);
    }

    // IsAnonymous
    public static void validateIsAnonymous(Boolean isAnonymous) {
        Assert.notNull(isAnonymous, IS_ANONYMOUS_IS_NULL);
    }

}
