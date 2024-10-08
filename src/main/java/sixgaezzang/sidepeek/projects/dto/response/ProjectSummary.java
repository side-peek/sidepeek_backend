package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import sixgaezzang.sidepeek.projects.domain.Project;

@Schema(description = "인기 프로젝트 정보")
public record ProjectSummary(
    @Schema(description = "프로젝트 식별자", example = "1")
    Long id,
    @Schema(description = "프로젝트 제목", example = "사이드픽👀")
    String name,
    @Schema(description = "프로젝트 부제목, 없으면 빈 문자열 반환", example = "요즘 사이드 플젝 뭐함? 사이드픽 \uD83D\uDC40")
    String subName,
    @Schema(description = "프로젝트 썸네일 이미지 URL, 없으면 빈 문자열 반환", example = "https://sidepeek.image/imageeUrl")
    String thumbnailUrl
) {

    public static ProjectSummary from(Project project) {
        return new ProjectSummary(
            project.getId(),
            project.getName(),
            project.getSubName(),
            project.getThumbnailUrl()
        );
    }

}
