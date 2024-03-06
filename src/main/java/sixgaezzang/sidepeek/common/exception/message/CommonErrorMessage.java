package sixgaezzang.sidepeek.common.exception.message;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_CATEGORY_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonErrorMessage {
    // Common
    public static final String LOGIN_IS_REQUIRED = "로그인이 필요합니다.";

    // Project, Comment Owner
    public static final String OWNER_ID_NOT_EQUALS_LOGIN_ID = "작성자 Id가 로그인한 회원 Id와 일치하지 않습니다.";

    // Github url
    public static final String GITHUB_URL_IS_INVALID = "Github URL 형식이 유효하지 않습니다.";
    public static final String GITHUB_URL_IS_NULL = "Github URL을 입력해주세요.";
    public static final String GITHUB_URL_OVER_MAX_LENGTH = "Github URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.";

    //
    public static final String TECH_STACKS_IS_NULL = "기술 스택들을 입력해주세요.";
    public static final String TECH_STACKS_OVER_MAX_COUNT =
        "기술 스택은 " + MAX_TECH_STACK_COUNT + "개 미만이어야 합니다.";
    public static final String CATEGORY_IS_NULL = "기술 스택 카테고리를 입력해주세요.";
    public static final String CATEGORY_OVER_MAX_LENGTH = "기술 스택 카테고리는 " + MAX_CATEGORY_LENGTH + "자 이하여야 합니다.";

}
