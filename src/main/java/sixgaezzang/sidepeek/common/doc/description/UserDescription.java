package sixgaezzang.sidepeek.common.doc.description;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_INTRODUCTION_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDescription {
    // UserControllerDoc
    public static final String PROJECTS_PAGE_NUMBER_DESCRIPTION = "조회할 페이지 번호 (기본값: 0)";
    public static final String PROJECTS_PAGE_SIZE_DESCRIPTION = "한 페이지의 크기 (기본값: 12)";
    public static final String PROJECTS_TYPE_DESCRIPTION =
        "프로젝트 조회 타입 [ JOINED(모두 가능), LIKED(본인만 가능), COMMENTED(본인만 가능) ]";

    // CheckEmailRequest, SignUpRequest, UpdatePasswordRequest
    public static final String EMAIL_DESCRIPTION = "이메일, 이메일 형식 검사";
    public static final String ORIGINAL_PASSWORD_DESCRIPTION = "기존 비밀번호, 기존 비밀번호와 일치 확인, 비밀번호 형식 검사";
    public static final String PASSWORD_DESCRIPTION = "새로운 비밀번호, 비밀번호 형식 검사";

    // CheckNicknameRequest, SignUpRequest, UpdateUserProfileRequest
    public static final String NICKNAME_DESCRIPTION = "닉네임, " + MAX_NICKNAME_LENGTH + "자 이하";
    public static final String USER_KEYWORD_DESCRIPTION = "검색어, " + MAX_NICKNAME_LENGTH + "자 이하";
    public static final String PROFILE_IMAGE_URL_DESCRIPTION = "프로필 이미지 URL, " + MAX_TEXT_LENGTH + "자 이하, URL 형식 검사";
    public static final String INTRODUCTION_DESCRIPTION = "소개, " + MAX_INTRODUCTION_LENGTH + "자 이하";
    public static final String JOB_DESCRIPTION = "직업 타입, [ 백엔드 개발자, 프론트엔드 개발자, IOS 개발자, 안드로이드 개발자, 디자이너, 데이터 분석가, 기타 ]";
    public static final String CAREER_DESCRIPTION = "경력 타입, [ 0년차, 1-3년차, 4-6년차, 7-9년차, 10년차 이상 ]";
    public static final String BLOG_URL_DESCRIPTION = "블로그 URL, " + MAX_TEXT_LENGTH + "자 이하, URL 형식 검사";
    public static final String USER_TECH_STACK_DESCRIPTION = "기술 스택 목록, " + MAX_TECH_STACK_COUNT + "개 이하(옵션)";
}
