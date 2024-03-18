package sixgaezzang.sidepeek.projects.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.File;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByProject(Project project);

    boolean existsByProject(Project project);

    void deleteAllByProject(Project project);

}
