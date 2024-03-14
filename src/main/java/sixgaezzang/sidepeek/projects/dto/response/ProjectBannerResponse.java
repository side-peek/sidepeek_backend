package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.Project;

@Schema(description = "배너용 프로젝트 응답")
@Builder
public record ProjectBannerResponse(
    @Schema(description = "프로젝트 식별자", example = "1")
    Long id,
    @Schema(description = "프로젝트 제목", example = "사이드픽👀")
    String name,
    @Schema(description = "프로젝트 부제목", example = "요즘 사이드 플젝 뭐함? 사이드픽 \uD83D\uDC40")
    String subName,
    @Schema(description = "프로젝트 썸네일 이미지 URL", example = "https://sidepeek.image/imageeUrl")
    String thumbnailUrl
) {

    public static ProjectBannerResponse from(Project project) {
        return ProjectBannerResponse.builder()
            .id(project.getId())
            .name(project.getName())
            .subName(project.getSubName())
            .thumbnailUrl(project.getThumbnailUrl())
            .build();
    }

}
