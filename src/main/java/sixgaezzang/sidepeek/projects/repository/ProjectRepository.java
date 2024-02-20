package sixgaezzang.sidepeek.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.projects.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
