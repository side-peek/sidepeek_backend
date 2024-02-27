package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateTextLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateURI;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;

import io.jsonwebtoken.lang.Assert;
import java.time.YearMonth;
import java.util.Objects;
import sixgaezzang.sidepeek.projects.domain.Project;

public class ProjectValidator {
    // Common
    public static void validateProject(Project project) { // TODO: validator 분리
        Assert.notNull(project, "프로젝트가 null 입니다.");
    }

    // Required
    public static void validateName(String name) {
        validateNotBlank(name, "프로젝트 제목을 입력해주세요.");
        validateMaxLength(name, MAX_PROJECT_NAME_LENGTH,
            "프로젝트 제목은 " + MAX_PROJECT_NAME_LENGTH + "자 이하여야 합니다.");
    }

    public static void validateOverview(String overview) {
        validateNotBlank(overview, "프로젝트 개요를 입력해주세요.");
        validateMaxLength(overview, MAX_OVERVIEW_LENGTH,
            "프로젝트 개요는 " + MAX_OVERVIEW_LENGTH + "자 이하여야 합니다.");
    }

    public static void validateGithubUrl(String githubUrl) {
        validateURI(githubUrl, "프로젝트 Github URL 형식이 유효하지 않습니다.");
        validateTextLength(githubUrl,
            "프로젝트 Github URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.");
    }

    public static void validateDescription(String description) {
        validateNotBlank(description, "프로젝트 기능 설명을 입력해주세요.");
        validateTextLength(description,
            "프로젝트 기능 설명은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.");
    }


    public static void validateOwnerId(Long ownerId) {
        Assert.notNull(ownerId, "프로젝트 게시글 작성자 Id를 명시해주세요.");
    }

    // Option
    public static void validateSubName(String subName) {
        if (Objects.nonNull(subName)) {
            validateMaxLength(subName, MAX_PROJECT_NAME_LENGTH,
                "프로젝트 부제목은 " + MAX_PROJECT_NAME_LENGTH + "자 이하여야 합니다.");
        }
    }

    public static void validateThumbnailUrl(String thumbnailUrl) {
        if (Objects.nonNull(thumbnailUrl)) {
            validateURI(thumbnailUrl, "프로젝트 썸네일 이미지 URL 형식이 유효하지 않습니다.");
            validateTextLength(thumbnailUrl,
                "프로젝트 썸네일 이미지 URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.");
        }
    }

    public static void validateDeployUrl(String deployUrl) {
        if (Objects.nonNull(deployUrl)) {
            validateURI(deployUrl, "프로젝트 배포 URL 형식이 유효하지 않습니다.");
            validateTextLength(deployUrl,
                "프로젝트 배포 URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.");
        }
    }

    public static void validateTroubleshooting(String troubleshooting) {
        if (Objects.nonNull(troubleshooting)) {
            validateTextLength(troubleshooting,
                "프로젝트 트러블 슈팅 설명은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.");
        }
    }

    public static void validateDuration(YearMonth startDate, YearMonth endDate) {
        if (Objects.isNull(startDate) && Objects.isNull(endDate)) {
            return; // 모두 null이면 넘어간다.
        }

        if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
            if (startDate.compareTo(endDate) >= 0) {
                throw new IllegalArgumentException("시작 날짜가 종료 날짜와 같거나 종료 날짜보다 이전이어야합니다.");
            }
            return;
        }

        // 둘 중 하나가 null이면 안된다. 둘 다 입력해야한다.
        throw new IllegalArgumentException("프로젝트 기간은 시작 날짜와 종료 날짜가 모두 기입되어야 합니다.");
    }

}
