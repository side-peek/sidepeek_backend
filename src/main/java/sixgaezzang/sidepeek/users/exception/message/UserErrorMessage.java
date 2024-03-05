package sixgaezzang.sidepeek.users.exception.message;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_CAREER_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_INTRODUCTION_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_JOB_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserErrorMessage {
    public static final String USER_NOT_EXISTING = "User Id에 해당하는 회원이 없습니다.";
    public static final String NICKNAME_IS_NULL = "닉네임을 입력해주세요.";
    public static final String NICKNAME_OVER_MAX_LENGTH = "닉네임은 " + MAX_NICKNAME_LENGTH + "자 이하여야 합니다.";
    public static final String INTRODUCTION_OVER_MAX_LENGTH = "소개글은 " + MAX_INTRODUCTION_LENGTH + "자 이하여야 합니다.";
    public static final String JOB_OVER_MAX_LENGTH = "직업은 " + MAX_JOB_LENGTH + "자 이하여야 합니다.";
    public static final String CAREER_OVER_MAX_LENGTH = "경력은 " + MAX_CAREER_LENGTH + "자 이하여야 합니다.";
    public static final String BLOG_URL_OVER_MAX_LENGTH = "블로그 URL은" + MAX_TEXT_LENGTH + "자 이하여야 합니다.";

}
