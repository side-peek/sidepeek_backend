package sixgaezzang.sidepeek.projects.dto.request;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;
import static sixgaezzang.sidepeek.common.util.Regex.URL_REGEXP;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.validator.constraints.URL;
import sixgaezzang.sidepeek.projects.domain.Project;

public record ProjectSaveRequest(
    // Required
    @Size(max = MAX_PROJECT_NAME_LENGTH,
        message = "프로젝트 제목은 " + MAX_PROJECT_NAME_LENGTH + "자를 넘을 수 없습니다.")
    @NotBlank(message = "프로젝트 제목을 입력해주세요.")
    String name,

    @Size(max = MAX_OVERVIEW_LENGTH,
        message = "프로젝트 개요는 " + MAX_OVERVIEW_LENGTH + "자를 넘을 수 없습니다.")
    @NotBlank(message = "프로젝트 개요를 입력해주세요.")
    String overview,

    @Min(value = MIN_ID, message = "작성자 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    @NotNull(message = "프로젝트 작성자 Id를 함께 보내주세요.")
    Long ownerId,

    @URL(message = "올바른 url 형식이 아닙니다.", regexp = URL_REGEXP)
    @NotBlank(message = "프로젝트 깃허브 url을 입력해주세요.")
    String githubUrl,

    @NotBlank(message = "프로젝트 기능 설명을 작성해주세요.")
    String description,

    @NotEmpty(message = "프로젝트에서 사용한 기술 스택을 등록해주세요.")
    List<ProjectSkillSaveRequest> techStacks,

    // Option
    @Size(max = MAX_PROJECT_NAME_LENGTH,
        message = "프로젝트 부제목은 " + MAX_PROJECT_NAME_LENGTH + "자를 넘을 수 없습니다.")
    String subName,

    @URL(message = "올바른 url 형식이 아닙니다.", regexp = URL_REGEXP)
    String thumbnailUrl,

    @URL(message = "올바른 url 형식이 아닙니다.", regexp = URL_REGEXP)
    String deployUrl,

    @JsonFormat(pattern = DATE_PATTERN)
    LocalDateTime startDate,

    @JsonFormat(pattern = DATE_PATTERN)
    LocalDateTime endDate,

    String troubleShooting,

    List<String> overviewImageUrls,

    List<MemberSaveRequest> members
) {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

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
