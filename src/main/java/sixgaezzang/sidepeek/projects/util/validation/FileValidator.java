package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.projects.exception.message.FileErrorMessage.OVERVIEW_IMAGE_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.exception.message.FileErrorMessage.OVERVIEW_IMAGE_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.FileErrorMessage.OVERVIEW_IMAGE_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import sixgaezzang.sidepeek.common.util.ValidationUtils;

public class FileValidator {

    public static void validateFiles(List<String> overviewImageUrls) {
        Assert.isTrue(overviewImageUrls.size() < MAX_OVERVIEW_IMAGE_COUNT, OVERVIEW_IMAGE_OVER_MAX_COUNT);
    }

    public static void validateFileUrl(String url) {
        ValidationUtils.validateURI(url, OVERVIEW_IMAGE_URL_IS_INVALID);
        ValidationUtils.validateTextLength(url, OVERVIEW_IMAGE_URL_OVER_MAX_LENGTH);
    }
}
