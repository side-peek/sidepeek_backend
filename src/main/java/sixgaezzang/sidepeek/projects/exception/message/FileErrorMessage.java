package sixgaezzang.sidepeek.projects.exception.message;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;

public class FileErrorMessage {
    public static final String OVERVIEW_IMAGE_OVER_MAX_COUNT =
        "프로젝트 레이아웃 이미지 수는 " + MAX_OVERVIEW_IMAGE_COUNT + "개 미만이어야 합니다.";
    public static final String OVERVIEW_IMAGE_URL_IS_INVALID = "프로젝트 레이아웃 이미지 URL 형식이 올바르지 않습니다.";
    public static final String OVERVIEW_IMAGE_URL_OVER_MAX_LENGTH =
        "프로젝트 레이아웃 이미지 URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.";
}
