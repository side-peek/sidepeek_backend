package sixgaezzang.sidepeek.common.doc.description;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectDescription {
    // CursorPaginationInfoRequest
    public static final String IS_RELEASED_DESCRIPTION = "출시 서비스만 보기(기본 - false)";
    public static final String SORT_DESCRIPTION = "정렬 조건 [ createdAt(default), view, like ]";
    public static final String PAGE_SIZE_DESCRIPTION = "한 페이지내 보여질 데이터의 개수";
    public static final String LAST_ORDER_COUNT_DESCRIPTION = "더보기 이전 마지막으로 보여진 좋아요수/조회수(첫 페이지면 null)";
    public static final String LAST_PROJECT_ID_DESCRIPTION = "더보기 이전 마지막으로 보여진 프로젝트 식별자(첫 페이지면 null)";

    // SaveProjectRequest, UpdateProjectRequest
    public static final String NAME_DESCRIPTION = "제목, " + MAX_PROJECT_NAME_LENGTH + "자 이하";
    public static final String OVERVIEW_DESCRIPTION = "개요, " + MAX_OVERVIEW_LENGTH + "자 이하";
    public static final String DESCRIPTION_DESCRIPTION = "기능 설명, " + MAX_TEXT_LENGTH + "자 이하";
    public static final String OWNER_ID_DESCRIPTION = "작성자 식별자, 로그인 회원 식별자와 일치 검사";
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

    // SaveMemberRequest
    public static final String MEMBER_ROLE_DESCRIPTION = "멤버 역할, " + MAX_ROLE_LENGTH + "자 이하";
    public static final String MEMBER_NICKNAME_DESCRIPTION = "멤버 닉네임, 회원도 설정 가능, " + MAX_NICKNAME_LENGTH + "자 이하";
    public static final String MEMBER_USER_ID_DESCRIPTION = "회원 멤버 유저 식별자(비회원 멤버이면 null)";


}
