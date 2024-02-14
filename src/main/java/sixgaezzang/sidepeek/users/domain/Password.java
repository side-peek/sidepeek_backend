package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.common.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.common.ValidationUtils.validatePassword;

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
        validateNotBlank(password, "비밀번호를 입력해주세요.");
        validatePassword(password, "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.");
        encoded = passwordEncoder.encode(password);
    }

    public boolean check(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, encoded);
    }
}
