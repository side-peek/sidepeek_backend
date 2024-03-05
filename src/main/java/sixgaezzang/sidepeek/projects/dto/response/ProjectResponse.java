package sixgaezzang.sidepeek.projects.dto.response;

import java.time.YearMonth;
import java.util.List;
import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.Project;

@Builder
public record ProjectResponse(
    Long id,
    String name,
    String subName,
    String overview,
    String thumbnailUrl,
    List<OverviewImageSummary> overviewImageUrl,
    String githubUrl,
    String deployUrl,
    Long viewCount,
    Long likeCount,
    List<ProjectSkillSummary> techStacks,
    YearMonth startDate,
    YearMonth endDate,
    Long ownerId,
    List<MemberSummary> members,
    String description,
    String troubleShooting
) {

    public static ProjectResponse from(Project project, List<OverviewImageSummary> overviewImageUrl,
                                       List<ProjectSkillSummary> techStacks, List<MemberSummary> members) {
        return ProjectResponse.builder()
            .id(project.getId())
            .name(project.getName())
            .subName(project.getSubName())
            .overview(project.getOverview())
            .thumbnailUrl(project.getThumbnailUrl())
            .overviewImageUrl(overviewImageUrl)
            .githubUrl(project.getGithubUrl())
            .deployUrl(project.getDeployUrl())
            .viewCount(project.getViewCount())
            .likeCount(project.getLikeCount())
            .techStacks(techStacks)
            .ownerId(project.getOwnerId())
            .startDate(project.getStartDate())
            .endDate(project.getEndDate())
            .members(members)
            .description(project.getDescription())
            .troubleShooting(project.getTroubleshooting())
            .build();
    }

}
