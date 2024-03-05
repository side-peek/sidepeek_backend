package sixgaezzang.sidepeek.common.exception.message;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;

public class CommonErrorMessage {
    // Github url
    public static final String GITHUB_URL_IS_INVALID = "Github URL 형식이 유효하지 않습니다.";
    public static final String GITHUB_URL_IS_NULL = "Github URL을 입력해주세요.";
    public static final String GITHUB_URL_OVER_MAX_LENGTH = "Github URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.";
}
