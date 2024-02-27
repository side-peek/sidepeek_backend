package sixgaezzang.sidepeek.projects.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;
import sixgaezzang.sidepeek.projects.util.validation.ProjectSkillValidator;
import sixgaezzang.sidepeek.projects.util.validation.ProjectValidator;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectSkillService {

    private final SkillRepository skillRepository;
    private final ProjectSkillRepository projectSkillRepository;

    @Transactional
    public List<ProjectSkillSummary> saveAll(Project project, List<ProjectSkillSaveRequest> techStacks) {
        ProjectValidator.validateProject(project);
        ProjectSkillValidator.validateTechStacks(techStacks);

        List<ProjectSkill> skills = techStacks.stream().map(
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

        return skills.stream()
            .map(ProjectSkillSummary::from)
            .toList();
    }


    public List<ProjectSkill> findAll(Project project) {
        return projectSkillRepository.findAllByProject(project);
    }
}
