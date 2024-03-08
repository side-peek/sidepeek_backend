package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validatePassword;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PASSWORD_FORMAT_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PASSWORD_IS_NULL;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    public static final String PASSWORD_REGXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&^]).{8,}$";

    @Column(name = "password", length = 100)
    private String encoded;

    public Password(String password, PasswordEncoder passwordEncoder) {
        validateNotBlank(password, PASSWORD_IS_NULL);
        validatePassword(password, PASSWORD_FORMAT_INVALID);
        encoded = passwordEncoder.encode(password);
    }

    public boolean check(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, encoded);
    }
}
