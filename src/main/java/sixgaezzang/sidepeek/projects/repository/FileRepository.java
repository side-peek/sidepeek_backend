package sixgaezzang.sidepeek.projects.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.File;
import sixgaezzang.sidepeek.projects.domain.file.FileType;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByProjectAndType(Project project, FileType type);

}
