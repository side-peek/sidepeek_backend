package sixgaezzang.sidepeek.users.util.validation;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_IS_NULL;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_ID_IS_NULL;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserValidator {

    public static void validateUserId(Long id) {
        Assert.notNull(id, USER_ID_IS_NULL);
    }

    public static void validateNickname(String nickname) {
        validateNotBlank(nickname, NICKNAME_IS_NULL);
        validateMaxLength(nickname, MAX_NICKNAME_LENGTH, NICKNAME_OVER_MAX_LENGTH);
    }

}
