package sixgaezzang.sidepeek.comments.util.validation;

import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CONTENT_IS_NULL;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CONTENT_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.comments.util.CommentConstant.MAX_CONTENT_LENGTH;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateNotBlank;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentValidator {

    public static void validateCommentContent(String content) {
        validateNotBlank(content, CONTENT_IS_NULL);
        validateMaxLength(content, MAX_CONTENT_LENGTH, CONTENT_OVER_MAX_LENGTH);
    }

}
