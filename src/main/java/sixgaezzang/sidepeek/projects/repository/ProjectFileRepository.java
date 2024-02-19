package sixgaezzang.sidepeek.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.projects.domain.file.File;

public interface ProjectFileRepository extends JpaRepository<File, Long> {
}
