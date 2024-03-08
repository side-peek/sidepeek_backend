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

    // Common
    public static final String USER_IS_NULL = "User가 null 입니다.";
    public static final String USER_ID_IS_NULL = "회원 Id를 입력해주세요.";
    public static final String USER_NOT_EXISTING = "회원 Id에 해당하는 회원이 없습니다.";
    public static final String USER_ALREADY_DELETED = "이미 삭제된 사용자입니다.";
    public static final String USER_ID_NOT_EQUALS_LOGIN_ID = "로그인한 사용자의 Id가 프로필 회원 Id와 일치하지 않습니다.";

    // Nickname
    public static final String NICKNAME_IS_NULL = "닉네임을 입력해주세요.";
    public static final String NICKNAME_OVER_MAX_LENGTH =
        "닉네임은 " + MAX_NICKNAME_LENGTH + "자 이하여야 합니다.";
    public static final String NICKNAME_DUPLICATE = "이미 사용중인 닉네임입니다.";

    // Profile Image URL
    public static final String PROFILE_IMAGE_URL_OVER_MAX_LENGTH =
        "프로필 이미지 url은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.";
    public static final String PROFILE_IMAGE_URL_IS_INVALID = "프로필 이미지 URL 형식이 유효하지 않습니다.";

    // Introduction
    public static final String INTRODUCTION_OVER_MAX_LENGTH =
        "소개글은 " + MAX_INTRODUCTION_LENGTH + "자 이하여야 합니다.";

    // Job
    public static final String JOB_IS_INVALID = "직업 정보가 유효하지 않습니다.";
    public static final String JOB_OVER_MAX_LENGTH = "직업은 " + MAX_JOB_LENGTH + "자 이하여야 합니다.";

    // Career
    public static final String CAREER_IS_INVALID = "경력 정보가 유효하지 않습니다.";
    public static final String CAREER_OVER_MAX_LENGTH = "경력은 " + MAX_CAREER_LENGTH + "자 이하여야 합니다.";
    public static final String EMAIL_FORMAT_INVALID = "이메일 형식이 올바르지 않습니다.";
    public static final String EMAIL_DUPLICATE = "이미 사용중인 이메일입니다.";
    public static final String PASSWORD_FORMAT_INVALID = "비밀번호 형식이 올바르지 않습니다.";
    public static final String PASSWORD_IS_NULL = "비밀번호를 입력해주세요.";

    // Blog URL
    public static final String BLOG_URL_OVER_MAX_LENGTH =
        "블로그 URL은" + MAX_TEXT_LENGTH + "자 이하여야 합니다.";
    public static final String BLOG_URL_IS_INVALID = "블로그 URL 형식이 유효하지 않습니다.";

}
