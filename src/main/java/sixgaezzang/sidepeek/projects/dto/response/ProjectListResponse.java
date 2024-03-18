package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.Project;

@Schema(description = "프로젝트 조회 정보")
@Builder
public record ProjectListResponse(
    @Schema(description = "프로젝트 식별자", example = "1")
    Long id,
    @Schema(description = "프로젝트 제목", example = "사이드픽👀")
    String name,
    @Schema(description = "프로젝트 부제목, 없으면 빈 문자열 반환", example = "요즘 사이드 플젝 뭐함? 사이드픽 \uD83D\uDC40")
    String subName,
    @Schema(description = "프로젝트 썸네일 이미지 URL, 없으면 빈 문자열 반환", example = "https://sidepeek.image/imageeUrl")
    String thumbnailUrl,
    @Schema(description = "프로젝트 조회수", example = "20")
    Long viewCount,
    @Schema(description = "프로젝트 좋아요수", example = "7")
    Long likeCount,
    @Schema(description = "로그인한 사용자가 좋아요한 프로젝트인지 여부", example = "false")
    boolean isLiked
) {

    public static ProjectListResponse from(Project project, boolean isLiked) {
        return ProjectListResponse.builder()
            .id(project.getId())
            .name(project.getName())
            .subName(project.getSubName())
            .thumbnailUrl(project.getThumbnailUrl())
            .viewCount(project.getViewCount())
            .likeCount(project.getLikeCount())
            .isLiked(isLiked)
            .build();
    }
}
