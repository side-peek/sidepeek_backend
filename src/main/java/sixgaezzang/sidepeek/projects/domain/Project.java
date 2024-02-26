package sixgaezzang.sidepeek.projects.domain;

import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateDeployUrl;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateDescription;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateDuration;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateGithubUrl;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateName;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateOverview;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateOwnerId;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateSubName;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateThumbnailUrl;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateTroubleshooting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import sixgaezzang.sidepeek.common.domain.BaseTimeEntity;

@Entity
@Table(name = "project")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = MAX_PROJECT_NAME_LENGTH)
    private String name;

    @Column(name = "sub_name", length = MAX_PROJECT_NAME_LENGTH)
    private String subName;

    @Column(name = "overview", nullable = false, length = MAX_OVERVIEW_LENGTH)
    private String overview;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime endDate;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(name = "deploy_url", columnDefinition = "TEXT")
    private String deployUrl;

    @Column(name = "github_url", columnDefinition = "TEXT")
    private String githubUrl;

    @Column(name = "owner_id", columnDefinition = "BIGINT", nullable = false)
    private Long ownerId; // TODO: User로 설정하는 것이 좋을까요? 놓는다면 [accessToken id 일치 확인 + 유저 존재 확인(추가 발생)] 해야합니다!

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "troubleshooting", columnDefinition = "TEXT")
    private String troubleshooting;

    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Project(String name, String subName, String overview, LocalDateTime startDate,
                   LocalDateTime endDate, Long ownerId, String thumbnailUrl,
                   String deployUrl, String githubUrl, String description, String troubleshooting) {
        validateConstructorRequiredArguments(name, overview, githubUrl, description, ownerId);
        validateConstructorOptionArguments(subName, thumbnailUrl, deployUrl, troubleshooting, startDate, endDate);

        // Required
        this.name = name;
        this.overview = overview;
        this.githubUrl = githubUrl;
        this.description = description;
        this.ownerId = ownerId;

        // Option
        this.subName = subName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnailUrl = thumbnailUrl;
        this.deployUrl = deployUrl;
        this.troubleshooting = troubleshooting;

        // Etc
        this.likeCount = 0L;
        this.viewCount = 0L;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    private void validateConstructorRequiredArguments(String name, String overview, String githubUrl,
                                                      String description, Long ownerId) {
        validateName(name);
        validateOverview(overview);
        validateGithubUrl(githubUrl);
        validateDescription(description);
        validateOwnerId(ownerId);
    }

    private void validateConstructorOptionArguments(String subName, String thumbnailUrl, String deployUrl,
                                                    String troubleshooting, LocalDateTime startDate,
                                                    LocalDateTime endDate) {
        validateSubName(subName);
        validateThumbnailUrl(thumbnailUrl);
        validateDeployUrl(deployUrl);
        validateTroubleshooting(troubleshooting);
        validateDuration(startDate, endDate);
    }

}
