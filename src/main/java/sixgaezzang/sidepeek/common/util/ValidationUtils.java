package sixgaezzang.sidepeek.common.util;

import static io.micrometer.common.util.StringUtils.isBlank;
import static io.micrometer.common.util.StringUtils.isNotBlank;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_IS_INVALID;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.LOGIN_IS_REQUIRED;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.common.util.Regex.URL_REGEXP;
import static sixgaezzang.sidepeek.users.domain.Password.PASSWORD_REGXP;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtils {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGXP);

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+[.][0-9A-Za-z]+$");

    private static final Pattern URI_PATTERN = Pattern.compile(URL_REGEXP);

    public static void validateLoginId(Long loginId) {
        if (Objects.isNull(loginId)) {
            throw new InvalidAuthenticationException(LOGIN_IS_REQUIRED);
        }
    }

    public static void validateEmail(String input, String message) {
        pattern(input, EMAIL_PATTERN, message);
    }

    public static void validateURI(String input, String message) {
        pattern(input, URI_PATTERN, message);
    }

    public static void validatePassword(String input, String message) {
        pattern(input, PASSWORD_PATTERN, message);
    }

    public static void validateMaxLength(String input, int maxLength, String message) {
        Assert.isTrue(input.length() <= maxLength, message);
    }

    public static void validateTextLength(String input, String message) {
        Assert.isTrue(input.length() <= MAX_TEXT_LENGTH, message);
    }

    public static void validateNotBlank(String input, String message) {
        Assert.isTrue(isNotBlank(input), message);
    }

    public static void validateBlank(String input, String message) {
        Assert.isTrue(isBlank(input), message);
    }

    public static <T> void validateNotNullAndEmpty(Collection<T> input, String message) {
        Assert.isTrue(isNotNullOrEmpty(input), message);
    }

    public static void validateGithubUrl(String githubUrl) {
        validateTextLength(githubUrl, GITHUB_URL_OVER_MAX_LENGTH);
        validateNotBlank(githubUrl, GITHUB_URL_IS_NULL);
        validateURI(githubUrl, GITHUB_URL_IS_INVALID);
    }

    public static void validateDeletedAt(LocalDateTime deletedAt) {
        // TODO: 프로젝트 삭제와 공통 로직
    }

    public static <T> boolean isNotNullOrEmpty(Collection<T> input) {
        return Objects.nonNull(input) && !input.isEmpty();
    }

    private static void pattern(String input, Pattern pattern, String message) {
        Assert.isTrue(pattern.matcher(input).matches(), message);
    }

}
