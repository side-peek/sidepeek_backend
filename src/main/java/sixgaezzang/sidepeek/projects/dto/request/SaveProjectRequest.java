package sixgaezzang.sidepeek.projects.dto.request;

import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_IS_INVALID;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;
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
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OWNER_ID_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.SUB_NAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.THUMBNAIL_URL_IS_INVALID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.THUMBNAIL_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.TROUBLESHOOTING_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectSkillErrorMessage.PROJECT_TECH_STACKS_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectSkillErrorMessage.PROJECT_TECH_STACKS_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_SKILL_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.YEAR_MONTH_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.YearMonth;
import java.util.List;
import org.hibernate.validator.constraints.URL;
import sixgaezzang.sidepeek.projects.domain.Project;

@Schema(description = "프로젝트 생성/수정 요청 정보")
public record SaveProjectRequest(
    // Required
    @Schema(description = "프로젝트 제목", example = "사이드픽👀")
    @Size(max = MAX_PROJECT_NAME_LENGTH, message = NAME_OVER_MAX_LENGTH)
    @NotBlank(message = NAME_IS_NULL)
    String name,

    @Schema(description = "프로젝트 개요", example = "사이드 프로젝트를 공유하는 사이드픽입니다.")
    @Size(max = MAX_OVERVIEW_LENGTH, message = OVERVIEW_OVER_MAX_LENGTH)
    @NotBlank(message = OVERVIEW_IS_NULL)
    String overview,

    @Schema(description = "프로젝트 작성자 Id(회원 식별자)", example = "1")
    @Min(value = MIN_ID, message = "작성자 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    @NotNull(message = OWNER_ID_IS_NULL)
    Long ownerId,

    @Schema(description = "프로젝트 Github URL", example = "https://github.com/side-peek")
    @Size(max = MAX_TEXT_LENGTH, message = GITHUB_URL_OVER_MAX_LENGTH)
    @URL(message = GITHUB_URL_IS_INVALID, regexp = URL_REGEXP)
    @NotBlank(message = GITHUB_URL_IS_NULL)
    String githubUrl,

    @Schema(description = "프로젝트 기능 설명", example = "## 사이드픽 기능 설명 Markdown")
    @Size(max = MAX_TEXT_LENGTH, message = DESCRIPTION_OVER_MAX_LENGTH)
    @NotBlank(message = DESCRIPTION_IS_NULL)
    String description,

    @Schema(description = "프로젝트 기술 스택")
    @Size(max = MAX_PROJECT_SKILL_COUNT, message = PROJECT_TECH_STACKS_OVER_MAX_COUNT)
    @NotEmpty(message = PROJECT_TECH_STACKS_IS_NULL)
    List<SaveProjectSkillRequest> techStacks,

    // Option
    @Schema(description = "프로젝트 부제목", example = "좋은 아이디어? 사이드픽에서 찾아봐!")
    @Size(max = MAX_PROJECT_NAME_LENGTH, message = SUB_NAME_OVER_MAX_LENGTH)
    String subName,

    @Schema(description = "프로젝트 썸네일 이미지 URL", example = "https://sidepeek.image/imageeUrl")
    @Size(max = MAX_TEXT_LENGTH, message = THUMBNAIL_URL_OVER_MAX_LENGTH)
    @URL(message = THUMBNAIL_URL_IS_INVALID, regexp = URL_REGEXP)
    String thumbnailUrl,

    @Schema(description = "프로젝트 배포 URL", example = "https://www.sidepeek.com")
    @Size(max = MAX_TEXT_LENGTH, message = DEPLOY_URL_OVER_MAX_LENGTH)
    @URL(message = DEPLOY_URL_IS_INVALID, regexp = URL_REGEXP)
    String deployUrl,

    @Schema(description = "프로젝트 시작 연-월", example = "2024-02")
    @JsonFormat(pattern = YEAR_MONTH_PATTERN)
    YearMonth startDate,

    @Schema(description = "프로젝트 종료 연-월", example = "2024-03")
    @JsonFormat(pattern = YEAR_MONTH_PATTERN)
    YearMonth endDate,

    @Schema(description = "프로젝트 트러블 슈팅", example = "## 사이드픽 트러블 슈팅 Markdown")
    @Size(max = MAX_TEXT_LENGTH, message = TROUBLESHOOTING_OVER_MAX_LENGTH)
    String troubleShooting,

    @Schema(description = "프로젝트 레이아웃 이미지 URL 목록", example = "[\"https://sidepeek.image/img1.jpg\"]")
    @Size(max = MAX_OVERVIEW_IMAGE_COUNT, message = OVERVIEW_IMAGE_OVER_MAX_COUNT)
    List<String> overviewImageUrls,

    @Schema(description = "프로젝트 레이아웃 멤버 목록")
    @Size(max = MAX_MEMBER_COUNT, message = MEMBER_OVER_MAX_COUNT)
    @NotEmpty(message = MEMBER_IS_EMPTY)
    List<SaveMemberRequest> members
) {

    public Project toEntity() {
        return Project.builder()
            .name(this.name)
            .subName(this.subName)
            .overview(this.overview)
            .ownerId(this.ownerId)
            .githubUrl(this.githubUrl)
            .description(this.description)
            .thumbnailUrl(this.thumbnailUrl)
            .deployUrl(this.deployUrl)
            .startDate(this.startDate)
            .endDate(this.endDate)
            .troubleshooting(this.troubleShooting)
            .build();
    }
}
