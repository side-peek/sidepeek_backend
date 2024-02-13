package sixgaezzang.sidepeek.projects.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "name", nullable = false, length = 300)
    private String name;

    @Column(name = "sub_name", length = 300)
    private String subName;

    @Column(name = "overview", nullable = false, length = 1000)
    private String overview;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP")
    private LocalDate startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP")
    private LocalDate endDate;

    @Column(name = "thumbnail_image_url", columnDefinition = "TEXT")
    private String thumbnailImageUrl;

    @Column(name = "deploy_url", columnDefinition = "TEXT")
    private String deployUrl;

    @Column(name = "github_url", columnDefinition = "TEXT")
    private String githubUrl;

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
    public Project(String name, String subName, String overview, LocalDate startDate, LocalDate endDate,
                   String thumbnailImageUrl, String deployUrl, String githubUrl, String description,
                   String troubleshooting,
                   Long likeCount, Long viewCount, LocalDateTime deletedAt) {
        this.name = name;
        this.subName = subName;
        this.overview = overview;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.deployUrl = deployUrl;
        this.githubUrl = githubUrl;
        this.description = description;
        this.troubleshooting = troubleshooting;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.deletedAt = deletedAt;
    }

}
