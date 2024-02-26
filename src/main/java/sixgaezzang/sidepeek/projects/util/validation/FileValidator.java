package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;

import io.jsonwebtoken.lang.Assert;
import java.util.List;

public class FileValidator {

    public static void validateFiles(List<String> overviewImageUrls) {
        Assert.isTrue(overviewImageUrls.size() <= MAX_OVERVIEW_IMAGE_COUNT,
            "프로젝트 레이아웃 이미지 수는 " + MAX_OVERVIEW_IMAGE_COUNT + "개를 넘을 수 없습니다.");
    }

}
