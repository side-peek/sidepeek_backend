package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "인기 프로젝트 리스트 응답")
@Builder
public record ProjectBannerResponse(
    List<ProjectSummary> projects
) {

    public static ProjectBannerResponse from(List<ProjectSummary> projects) {
        return ProjectBannerResponse.builder()
            .projects(projects)
            .build();
    }
}
