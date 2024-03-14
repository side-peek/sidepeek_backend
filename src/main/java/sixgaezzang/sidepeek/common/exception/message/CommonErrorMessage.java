package sixgaezzang.sidepeek.common.exception.message;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonErrorMessage {

    // Common
    public static final String LOGIN_IS_REQUIRED = "로그인이 필요합니다.";
    public static final String ERROR_OCCURRED_WRITING_JSON = "JSON 작성 중 오류가 발생했습니다.";
    public static final String ERROR_OCCURRED_SENDING_RESPONSE = "에러 응답 전송 중 에러가 발생했습니다.";
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러가 발생했습니다.";

    // Project, Comment Owner
    public static final String OWNER_ID_NOT_EQUALS_LOGIN_ID = "작성자 Id가 로그인한 회원 Id와 일치하지 않습니다.";

    // Github url
    public static final String GITHUB_URL_IS_INVALID = "Github URL 형식이 유효하지 않습니다.";
    public static final String GITHUB_URL_IS_NULL = "Github URL을 입력해주세요.";
    public static final String GITHUB_URL_OVER_MAX_LENGTH =
        "Github URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.";

}
