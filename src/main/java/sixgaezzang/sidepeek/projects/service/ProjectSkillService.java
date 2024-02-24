package sixgaezzang.sidepeek.projects.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectSkillService {

    private final SkillRepository skillRepository;
    private final ProjectSkillRepository projectSkillRepository;

    @Transactional
    public void saveAll(Project project, List<ProjectSkillSaveRequest> projectSkillSaveRequests) {
        List<ProjectSkill> skills = projectSkillSaveRequests.stream().map(
            projectSkill -> {
                Skill skill = skillRepository.findById(projectSkill.skillId())
                    .orElseThrow(() -> new EntityNotFoundException("Skill Id에 해당하는 스킬이 없습니다."));

                return ProjectSkill.builder()
                    .project(project)
                    .skill(skill)
                    .category(projectSkill.category())
                    .build();
            }
        ).toList();

        projectSkillRepository.saveAll(skills);
    }
}
