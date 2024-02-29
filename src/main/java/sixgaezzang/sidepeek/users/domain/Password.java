package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validatePassword;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.BLANK_PASSWORD;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.INVALID_PASSWORD_FORMAT;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    public static final String PASSWORD_REGXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&^]).{8,}$";

    @Column(name = "password", length = 100)
    private String encoded;

    public Password(String password, PasswordEncoder passwordEncoder) {
        validateNotBlank(password, BLANK_PASSWORD.getMessage());
        validatePassword(password, INVALID_PASSWORD_FORMAT.getMessage());
        encoded = passwordEncoder.encode(password);
    }

    public boolean check(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, encoded);
    }
}
