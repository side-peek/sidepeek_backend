package sixgaezzang.sidepeek.projects.domain;

import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_ALREADY_DELETED;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import sixgaezzang.sidepeek.common.domain.BaseTimeEntity;
import sixgaezzang.sidepeek.common.util.ValidationUtils;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.util.converter.YearMonthDateAttributeConverter;
import sixgaezzang.sidepeek.projects.util.validation.ProjectValidator;

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

    @Column(name = "start_date", columnDefinition = "DATE")
    @Convert(converter = YearMonthDateAttributeConverter.class)
    private YearMonth startDate;

    @Column(name = "end_date", columnDefinition = "DATE")
    @Convert(converter = YearMonthDateAttributeConverter.class)
    private YearMonth endDate;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(name = "deploy_url", columnDefinition = "TEXT")
    private String deployUrl;

    @Column(name = "github_url", columnDefinition = "TEXT")
    private String githubUrl;

    @Column(name = "owner_id", columnDefinition = "BIGINT", nullable = false)
    private Long ownerId;

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
    public Project(String name, String subName, String overview, YearMonth startDate, YearMonth endDate, Long ownerId,
                   String thumbnailUrl, String deployUrl, String githubUrl, String description,
                   String troubleshooting) {
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

    public void softDelete() {
        if (Objects.isNull(this.deletedAt)) {
            this.deletedAt = LocalDateTime.now();
            return;
        }
        throw new IllegalStateException(PROJECT_ALREADY_DELETED);
    }

    private void validateConstructorRequiredArguments(String name, String overview, String githubUrl,
                                                      String description, Long ownerId) {
        ProjectValidator.validateName(name);
        ProjectValidator.validateOverview(overview);
        ValidationUtils.validateGithubUrl(githubUrl);
        ProjectValidator.validateDescription(description);
        ProjectValidator.validateOwnerId(ownerId);
    }

    private void validateConstructorOptionArguments(String subName, String thumbnailUrl, String deployUrl,
                                                    String troubleshooting, YearMonth startDate, YearMonth endDate) {
        ProjectValidator.validateSubName(subName);
        ProjectValidator.validateThumbnailUrl(thumbnailUrl);
        ProjectValidator.validateDeployUrl(deployUrl);
        ProjectValidator.validateTroubleshooting(troubleshooting);
        ProjectValidator.validateDuration(startDate, endDate);
    }

    // TODO: User 처럼 Private Setter(Lombok X) 구현하는 것이 나을까 아래와 같은 방식으로 하는 게 나을까?
    public Project update(SaveProjectRequest request) {
        validateConstructorRequiredArguments(request.name(), request.overview(), request.githubUrl(),
            request.description(), request.ownerId());
        validateConstructorOptionArguments(request.subName(), request.thumbnailUrl(), request.deployUrl(),
            request.troubleShooting(), request.startDate(), request.endDate());

        // Required
        this.name = request.name();
        this.overview = request.overview();
        this.githubUrl = request.githubUrl();
        this.description = request.description();
        this.ownerId = request.ownerId();

        // Option
        this.subName = request.subName();
        this.startDate = request.startDate();
        this.endDate = request.endDate();
        this.thumbnailUrl = request.thumbnailUrl();
        this.deployUrl = request.deployUrl();
        this.troubleshooting = request.troubleShooting();

        return this;
    }
}
