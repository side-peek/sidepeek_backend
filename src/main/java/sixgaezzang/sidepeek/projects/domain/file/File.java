package sixgaezzang.sidepeek.projects.domain.file;

import static sixgaezzang.sidepeek.projects.util.validation.FileValidator.validateFileUrl;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateProject;

import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.projects.domain.Project;

@Entity
@Table(name = "files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "type", nullable = false, length = 30, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    private FileType type;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Builder
    public File(Project project, FileType type, String url) {
        validateConstructorArguments(project, type, url);
        this.project = project;
        this.type = type;
        this.url = url;
    }

    private void validateConstructorArguments(Project project, FileType type, String url) {
        validateProject(project);
        Assert.notNull(type, "type을 입력해주세요.");
        validateFileUrl(url);
    }

}
