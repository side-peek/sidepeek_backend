package sixgaezzang.sidepeek.projects.dto.request;

import static sixgaezzang.sidepeek.common.doc.description.CommonDescription.GITHUB_URL_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.DEPLOY_URL_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.DESCRIPTION_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.END_DATE_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.NAME_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.OVERVIEW_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.OVERVIEW_IMAGE_URLS_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.PROJECT_TECH_STACK_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.START_DATE_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.SUB_NAME_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.THUMBNAIL_URL_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.TROUBLE_SHOOTING_DESCRIPTION;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_IS_INVALID;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.TECH_STACKS_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.TECH_STACKS_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.common.util.Regex.URL_REGEXP;
import static sixgaezzang.sidepeek.projects.exception.message.FileErrorMessage.OVERVIEW_IMAGE_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_IS_EMPTY;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DEPLOY_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DEPLOY_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DESCRIPTION_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.DESCRIPTION_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.NAME_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.NAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OVERVIEW_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OVERVIEW_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.SUB_NAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.THUMBNAIL_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.THUMBNAIL_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.TROUBLESHOOTING_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.YEAR_MONTH_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.YearMonth;
import java.util.List;
import org.hibernate.validator.constraints.URL;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;

@Schema(description = "프로젝트 수정 요청")
public record UpdateProjectRequest(
    // Required
    @Schema(description = NAME_DESCRIPTION, example = "사이드픽👀")
    @Size(max = MAX_PROJECT_NAME_LENGTH, message = NAME_OVER_MAX_LENGTH)
    @NotBlank(message = NAME_IS_NULL)
    String name,

    @Schema(description = OVERVIEW_DESCRIPTION, example = "사이드 프로젝트를 공유하는 사이드픽입니다.")
    @Size(max = MAX_OVERVIEW_LENGTH, message = OVERVIEW_OVER_MAX_LENGTH)
    @NotBlank(message = OVERVIEW_IS_NULL)
    String overview,

    @Schema(description = GITHUB_URL_DESCRIPTION, example = "https://github.com/side-peek")
    @Size(max = MAX_TEXT_LENGTH, message = GITHUB_URL_OVER_MAX_LENGTH)
    @URL(message = GITHUB_URL_IS_INVALID, regexp = URL_REGEXP)
    @NotBlank(message = GITHUB_URL_IS_NULL)
    String githubUrl,

    @Schema(description = DESCRIPTION_DESCRIPTION, example = "## 사이드픽 기능 설명 Markdown")
    @Size(max = MAX_TEXT_LENGTH, message = DESCRIPTION_OVER_MAX_LENGTH)
    @NotBlank(message = DESCRIPTION_IS_NULL)
    String description,

    @Schema(description = PROJECT_TECH_STACK_DESCRIPTION)
    @Size(max = MAX_TECH_STACK_COUNT, message = TECH_STACKS_OVER_MAX_COUNT)
    @NotEmpty(message = TECH_STACKS_IS_NULL)
    List<SaveTechStackRequest> techStacks,

    // Option
    @Schema(description = SUB_NAME_DESCRIPTION, example = "좋은 아이디어? 사이드픽에서 찾아봐!")
    @Size(max = MAX_PROJECT_NAME_LENGTH, message = SUB_NAME_OVER_MAX_LENGTH)
    String subName,

    @Schema(description = THUMBNAIL_URL_DESCRIPTION, example = "https://sidepeek.image/imageeUrl")
    @Size(max = MAX_TEXT_LENGTH, message = THUMBNAIL_URL_OVER_MAX_LENGTH)
    @URL(message = THUMBNAIL_URL_IS_INVALID, regexp = URL_REGEXP)
    String thumbnailUrl,

    @Schema(description = DEPLOY_URL_DESCRIPTION, example = "https://www.sidepeek.com")
    @Size(max = MAX_TEXT_LENGTH, message = DEPLOY_URL_OVER_MAX_LENGTH)
    @URL(message = DEPLOY_URL_IS_INVALID, regexp = URL_REGEXP)
    String deployUrl,

    @Schema(description = START_DATE_DESCRIPTION, example = "2024-02")
    @JsonFormat(pattern = YEAR_MONTH_PATTERN)
    YearMonth startDate,

    @Schema(description = END_DATE_DESCRIPTION, example = "2024-03")
    @JsonFormat(pattern = YEAR_MONTH_PATTERN)
    YearMonth endDate,

    @Schema(description = TROUBLE_SHOOTING_DESCRIPTION, example = "## 사이드픽 트러블 슈팅 Markdown")
    @Size(max = MAX_TEXT_LENGTH, message = TROUBLESHOOTING_OVER_MAX_LENGTH)
    String troubleShooting,

    @Schema(description = OVERVIEW_IMAGE_URLS_DESCRIPTION, example = "[\"https://sidepeek.image/img1.jpg\"]")
    @Size(max = MAX_OVERVIEW_IMAGE_COUNT, message = OVERVIEW_IMAGE_OVER_MAX_COUNT)
    List<String> overviewImageUrls,

    @Schema(description = OVERVIEW_IMAGE_URLS_DESCRIPTION)
    @Size(max = MAX_MEMBER_COUNT, message = MEMBER_OVER_MAX_COUNT)
    @NotEmpty(message = MEMBER_IS_EMPTY)
    List<SaveMemberRequest> members
) {
}
