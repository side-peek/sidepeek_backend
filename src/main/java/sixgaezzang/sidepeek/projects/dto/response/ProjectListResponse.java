package sixgaezzang.sidepeek.projects.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.Project;

@Builder
public record ProjectListResponse(
    Long id,
    String name,
    String subName,
    String thumbnailUrl,
    Long viewCount,
    Long likeCount,
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
