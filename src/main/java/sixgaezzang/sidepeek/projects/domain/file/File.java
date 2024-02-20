package sixgaezzang.sidepeek.projects.domain.file;

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
        this.project = project;
        this.type = type;
        this.url = url;
    }

}
