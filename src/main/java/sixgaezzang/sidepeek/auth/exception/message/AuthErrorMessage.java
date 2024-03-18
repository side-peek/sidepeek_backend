package sixgaezzang.sidepeek.auth.exception.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthErrorMessage {

    public static final String EMAIL_IS_NULL = "이메일을 입력해주세요.";
    public static final String PASSWORD_IS_NULL = "비밀번호를 입력해주세요.";
    public static final String REFRESH_TOKEN_IS_NULL = "Refresh Token을 입력해주세요.";
    public static final String TOKEN_IS_INVALID = "유효하지 않은 토큰입니다.";
    public static final String PASSWORD_NOT_MATCH = "비밀번호가 일치하지 않습니다.";
    public static final String OAUTH_USER_TYPE_IS_INVALID = "유효하지 않은 OAuth 사용자 타입입니다.";

}
