package sixgaezzang.sidepeek.common;

import static io.micrometer.common.util.StringUtils.isBlank;
import static io.micrometer.common.util.StringUtils.isNotBlank;

import java.util.regex.Pattern;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+[.][0-9A-Za-z]+$");

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&^]).{8,}$");

    private static final Pattern URI_PATTERN = Pattern.compile(
        "^(https?)://([^:/\\s]+)(:([^/]*))?((/[^\\s/]+)*)?/?([^#\\s?]*)(\\?([^#\\s]*))?(#(\\w*))?$");

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

    public static void validateNotBlank(String input, String message) {
        Assert.isTrue(isNotBlank(input), message);
    }

    public static void validateBlank(String input, String message) {
        Assert.isTrue(isBlank(input), message);
    }

    private static void pattern(String input, Pattern pattern, String message) {
        Assert.isTrue(pattern.matcher(input).matches(), message);
    }

}
