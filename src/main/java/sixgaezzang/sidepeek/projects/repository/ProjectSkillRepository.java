package sixgaezzang.sidepeek.projects.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;

public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, Long> {

    List<ProjectSkill> findAllByProject(Project project);

    void deleteAllByProject(Project project);

    boolean existsByProject(Project project);
}
