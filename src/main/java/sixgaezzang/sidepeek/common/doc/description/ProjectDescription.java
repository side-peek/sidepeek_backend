package sixgaezzang.sidepeek.common.doc.description;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectDescription {
    public static final String NAME_DESCRIPTION = "제목, " + MAX_PROJECT_NAME_LENGTH + "자 이하";
    public static final String OVERVIEW_DESCRIPTION = "개요, " + MAX_OVERVIEW_LENGTH + "자 이하";
    public static final String DESCRIPTION_DESCRIPTION = "기능 설명, " + MAX_TEXT_LENGTH + "자 이하";
    public static final String OWNER_ID_DESCRIPTION = "작성자 식별자 ";
    public static final String PROJECT_TECH_STACK_DESCRIPTION =
        "기술 스택, " + MAX_TECH_STACK_COUNT + "개 이하, 같은 카테고리 내 기술 스택 중복 불가";
    public static final String MEMBER_DESCRIPTION =
        "멤버 목록, " + MAX_MEMBER_COUNT + "명 이하(작성자 본인 포함), 같은 역할 내 닉네임 중복 불가, 비회원 userId는 null";
    public static final String SUB_NAME_DESCRIPTION = "부제목, " + MAX_PROJECT_NAME_LENGTH + "자 이하";
    public static final String THUMBNAIL_URL_DESCRIPTION =
        "썸네일 이미지 URL, " + MAX_TEXT_LENGTH + "자 이하, 값이 있을 경우 URL 형식 검사";
    public static final String DEPLOY_URL_DESCRIPTION =
        "배포 URL, " + MAX_TEXT_LENGTH + "자 이하, 값이 있을 경우 URL 형식 검사";
    public static final String START_DATE_DESCRIPTION = "시작 연월, YYYY_MM 형식, 종료 연월과 함께 보내거나 모두 null";
    public static final String END_DATE_DESCRIPTION = "종료 연월, YYYY_MM 형식, 종료 연월과 함께 보내거나 모두 null";
    public static final String TROUBLE_SHOOTING_DESCRIPTION = "트러블 슈팅, " + MAX_TEXT_LENGTH + "자 이하";
    public static final String OVERVIEW_IMAGE_URLS_DESCRIPTION =
        "레이아웃 이미지 URL 목록, " + MAX_OVERVIEW_IMAGE_COUNT + "개 이하, 배열에 값이 있다면 URL 형식 검사";
}
