package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateTextLength;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateURI;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DEPLOY_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DEPLOY_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DESCRIPTION_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DESCRIPTION_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DURATION_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DURATION_IS_REVERSED;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.NAME_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.NAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OVERVIEW_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OVERVIEW_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OWNER_ID_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.SUB_NAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.THUMBNAIL_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.THUMBNAIL_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.TROUBLESHOOTING_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;

import io.jsonwebtoken.lang.Assert;
import io.micrometer.common.util.StringUtils;
import java.time.YearMonth;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.projects.domain.Project;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectValidator {

    // Common
    public static void validateProject(Project project) {
        Assert.notNull(project, PROJECT_IS_NULL);
    }

    // Required
    public static void validateName(String name) {
        validateNotBlank(name, NAME_IS_NULL);
        validateMaxLength(name, MAX_PROJECT_NAME_LENGTH, NAME_OVER_MAX_LENGTH);
    }

    public static void validateOverview(String overview) {
        validateNotBlank(overview, OVERVIEW_IS_NULL);
        validateMaxLength(overview, MAX_OVERVIEW_LENGTH, OVERVIEW_OVER_MAX_LENGTH);
    }

    public static void validateDescription(String description) {
        validateNotBlank(description, DESCRIPTION_IS_NULL);
        validateTextLength(description, DESCRIPTION_OVER_MAX_LENGTH);
    }

    public static void validateOwnerId(Long ownerId) {
        Assert.notNull(ownerId, OWNER_ID_IS_NULL);
    }

    // Option
    public static void validateSubName(String subName) {
        if (StringUtils.isNotBlank(subName)) {
            validateMaxLength(subName, MAX_PROJECT_NAME_LENGTH, SUB_NAME_OVER_MAX_LENGTH);
        }
    }

    public static void validateThumbnailUrl(String thumbnailUrl) {
        if (StringUtils.isNotBlank(thumbnailUrl)) {
            validateTextLength(thumbnailUrl, THUMBNAIL_URL_OVER_MAX_LENGTH);
            validateURI(thumbnailUrl, THUMBNAIL_URL_IS_INVALID);
        }
    }

    public static void validateDeployUrl(String deployUrl) {
        if (StringUtils.isNotBlank(deployUrl)) {
            validateURI(deployUrl, DEPLOY_URL_IS_INVALID);
            validateTextLength(deployUrl, DEPLOY_URL_OVER_MAX_LENGTH);
        }
    }

    public static void validateTroubleshooting(String troubleshooting) {
        if (StringUtils.isNotBlank(troubleshooting)) {
            validateTextLength(troubleshooting, TROUBLESHOOTING_OVER_MAX_LENGTH);
        }
    }

    public static void validateDuration(YearMonth startDate, YearMonth endDate) {
        if (Objects.isNull(startDate) && Objects.isNull(endDate)) {
            return; // 모두 null이면 넘어간다.
        }

        if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
            if (startDate.compareTo(endDate) > 0) {
                throw new IllegalArgumentException(DURATION_IS_REVERSED);
            }
            return;
        }

        // 둘 중 하나가 null이면 안된다. 둘 다 입력해야한다.
        throw new IllegalArgumentException(DURATION_IS_INVALID);
    }

}
