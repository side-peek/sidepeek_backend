package sixgaezzang.sidepeek.common.doc.description;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_INTRODUCTION_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDescription {
    public static final String USER_KEYWORD_DESCRIPTION = "검색어, " + MAX_NICKNAME_LENGTH + "자 이하";
    public static final String NICKNAME_DESCRIPTION = "닉네임, " + MAX_NICKNAME_LENGTH + "자 이하";
    public static final String PROFILE_IMAGE_URL_DESCRIPTION = "프로필 이미지 URL, " + MAX_TEXT_LENGTH + "자 이하, URL 형식 검사";
    public static final String INTRODUCTION_DESCRIPTION = "소개, " + MAX_INTRODUCTION_LENGTH + "자 이하";
    public static final String JOB_DESCRIPTION = "직업 타입, [ 백엔드 개발자, 프론트엔드 개발자, IOS 개발자, 안드로이드 개발자, 디자이너, 데이터 분석가, 기타 ]";
    public static final String CAREER_DESCRIPTION = "경력 타입, [ 0년차, 1-3년차, 4-6년차, 7-9년차, 10년차 이상 ]";
    public static final String BLOG_URL_DESCRIPTION = "블로그 URL, " + MAX_TEXT_LENGTH + "자 이하, URL 형식 검사";
    public static final String USER_TECH_STACK_DESCRIPTION = "기술 스택 목록, " + MAX_TECH_STACK_COUNT + "개 이하(옵션)";
}
