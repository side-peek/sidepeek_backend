package sixgaezzang.sidepeek.projects.exception.message;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectErrorMessage {
    // Project
    public static final String PROJECT_IS_NULL = "프로젝트가 null 입니다.";
    public static final String ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE = "게사글 작성자와 회원 멤버만이 수정할 수 있습니다.";

    // Name
    public static final String NAME_IS_NULL = "프로젝트 제목을 입력해주세요.";
    public static final String NAME_OVER_MAX_LENGTH = "프로젝트 제목은 " + MAX_PROJECT_NAME_LENGTH + "자 이하여야 합니다.";

    // Overview
    public static final String OVERVIEW_IS_NULL = "프로젝트 개요를 입력해주세요.";
    public static final String OVERVIEW_OVER_MAX_LENGTH = "프로젝트 개요는 " + MAX_OVERVIEW_LENGTH + "자 이하여야 합니다.";

    // Sub Name
    public static final String SUB_NAME_OVER_MAX_LENGTH = "프로젝트 부제목은 " + MAX_PROJECT_NAME_LENGTH + "자 이하여야 합니다.";

    // Deploy url
    public static final String DEPLOY_URL_OVER_MAX_LENGTH = "프로젝트 배포 URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.";
    public static final String DEPLOY_URL_IS_INVALID = "프로젝트 배포 URL 형식이 유효하지 않습니다.";

    // Troubleshooting
    public static final String TROUBLESHOOTING_OVER_MAX_LENGTH = "프로젝트 트러블 슈팅 설명은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.";

    // Thumbnail url
    public static final String THUMBNAIL_URL_IS_INVALID = "프로젝트 썸네일 이미지 URL 형식이 유효하지 않습니다.";
    public static final String THUMBNAIL_URL_OVER_MAX_LENGTH = "프로젝트 썸네일 이미지 URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.";

    // Owner Id
    public static final String OWNER_ID_IS_NULL = "프로젝트 게시글 작성자 Id를 입력해주세요.";

    // Description
    public static final String DESCRIPTION_IS_NULL = "프로젝트 기능 설명을 입력해주세요.";
    public static final String DESCRIPTION_OVER_MAX_LENGTH = "프로젝트 기능 설명은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.";

    // Duration
    public static final String DURATION_IS_REVERSED = "시작 날짜가 종료 날짜와 같거나 종료 날짜보다 이전이어야합니다.";
    public static final String DURATION_IS_INVALID = "프로젝트 기간은 시작 날짜와 종료 날짜가 모두 기입되어야 합니다.";
}
