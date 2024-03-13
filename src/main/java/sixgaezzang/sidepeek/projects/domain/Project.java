package sixgaezzang.sidepeek.projects.domain;

import static sixgaezzang.sidepeek.common.util.SetUtils.getBlankIfNullOrBlank;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateRequiredGithubUrl;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_ALREADY_DELETED;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateDeployUrl;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateDescription;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateDuration;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateName;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateOverview;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateOwnerId;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateSubName;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateThumbnailUrl;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateTroubleshooting;

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
import sixgaezzang.sidepeek.projects.dto.request.UpdateProjectRequest;
import sixgaezzang.sidepeek.projects.util.converter.YearMonthDateAttributeConverter;

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
        this.subName = getBlankIfNullOrBlank(subName);
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnailUrl = getBlankIfNullOrBlank(thumbnailUrl);
        this.deployUrl = getBlankIfNullOrBlank(deployUrl);
        this.troubleshooting = getBlankIfNullOrBlank(troubleshooting);

        // Etc
        this.likeCount = 0L;
        this.viewCount = 0L;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void softDelete(LocalDateTime now) {
        if (Objects.isNull(this.deletedAt)) {
            this.deletedAt = now;
            return;
        }
        throw new IllegalStateException(PROJECT_ALREADY_DELETED);
    }

    private void validateConstructorRequiredArguments(String name, String overview, String githubUrl,
                                                      String description, Long ownerId) {
        validateName(name);
        validateOverview(overview);
        validateRequiredGithubUrl(githubUrl);
        validateDescription(description);
        validateOwnerId(ownerId);
    }

    private void validateConstructorOptionArguments(String subName, String thumbnailUrl, String deployUrl,
                                                    String troubleshooting, YearMonth startDate, YearMonth endDate) {
        validateSubName(subName);
        validateThumbnailUrl(thumbnailUrl);
        validateDeployUrl(deployUrl);
        validateTroubleshooting(troubleshooting);
        validateDuration(startDate, endDate);
    }

    public Project update(UpdateProjectRequest request) {
        // Required
        setName(request.name());
        setOverview(request.overview());
        setGithubUrl(request.githubUrl());
        setDescription(request.description());

        // Option
        setSubName(request.subName());
        setDuration(request.startDate(), request.endDate());
        setThumbnailUrl(request.thumbnailUrl());
        setDeployUrl(request.deployUrl());
        setTroubleshooting(request.troubleShooting());

        return this;
    }

    // Required
    private void setName(String name) {
        validateName(name);
        this.name = name;
    }

    private void setOverview(String overview) {
        validateOverview(overview);
        this.overview = overview;
    }

    private void setGithubUrl(String githubUrl) {
        validateRequiredGithubUrl(githubUrl);
        this.githubUrl = githubUrl;

    }

    private void setDescription(String description) {
        validateDescription(description);
        this.description = description;

    }

    // Option
    private void setSubName(String subName) {
        validateSubName(subName);
        this.subName = getBlankIfNullOrBlank(subName);

    }

    private void setDuration(YearMonth startDate, YearMonth endDate) {
        validateDuration(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void setThumbnailUrl(String thumbnailUrl) {
        validateThumbnailUrl(thumbnailUrl);
        this.thumbnailUrl = getBlankIfNullOrBlank(thumbnailUrl);
    }

    private void setDeployUrl(String deployUrl) {
        validateDeployUrl(deployUrl);
        this.deployUrl = getBlankIfNullOrBlank(deployUrl);

    }

    private void setTroubleshooting(String troubleShooting) {
        validateTroubleshooting(troubleshooting);
        this.troubleshooting = getBlankIfNullOrBlank(troubleShooting);
    }

}
