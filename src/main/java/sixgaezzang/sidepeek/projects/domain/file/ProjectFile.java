package sixgaezzang.sidepeek.projects.domain.file;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProjectFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 30, columnDefinition = "VARCHAR(30)")
    @Enumerated(EnumType.STRING)
    private FileType type;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Builder
    public ProjectFile(Long id, FileType type, String url) {
        this.id = id;
        this.type = type;
        this.url = url;
    }

}
