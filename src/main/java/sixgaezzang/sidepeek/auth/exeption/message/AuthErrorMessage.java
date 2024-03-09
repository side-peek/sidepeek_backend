package sixgaezzang.sidepeek.auth.exeption.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthErrorMessage {

    public static final String EMAIL_IS_NULL = "이메일을 입력해주세요.";
    public static final String PASSWORD_IS_NULL = "비밀번호를 입력해주세요.";
    public static final String REFRESH_TOKEN_IS_NULL = "Refresh Token을 입력해주세요.";
}
