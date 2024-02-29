package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import sixgaezzang.sidepeek.common.util.ValidationUtils;

public class FileValidator {

    public static void validateFiles(List<String> overviewImageUrls) {
        Assert.isTrue(overviewImageUrls.size() < MAX_OVERVIEW_IMAGE_COUNT,
            "프로젝트 레이아웃 이미지 수는 " + MAX_OVERVIEW_IMAGE_COUNT + "개 미만이어야 합니다.");
    }

    public static void validateFileUrl(String url) {
        ValidationUtils.validateURI(url, "프로젝트 레이아웃 이미지 URL 형식이 올바르지 않습니다.");
        ValidationUtils.validateTextLength(url, "프로젝트 레이아웃 이미지 URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.");
    }
}
