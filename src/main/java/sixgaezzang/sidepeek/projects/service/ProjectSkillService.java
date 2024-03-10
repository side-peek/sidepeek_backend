package sixgaezzang.sidepeek.projects.service;

import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.TECH_STACKS_IS_NULL;
import static sixgaezzang.sidepeek.common.util.validation.TechStackValidator.validateTechStacks;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateNotNullAndEmpty;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateProject;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_NOT_EXISTING;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.common.dto.request.UpdateUserSkillRequest;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectSkillService {

    private final SkillRepository skillRepository;
    private final ProjectSkillRepository projectSkillRepository;

    public List<ProjectSkill> findAll(Project project) {
        return projectSkillRepository.findAllByProject(project);
    }

    @Transactional
    public List<ProjectSkillSummary> saveAll(Project project, List<UpdateUserSkillRequest> techStacks) {
        validateProject(project);
        validateNotNullAndEmpty(techStacks, TECH_STACKS_IS_NULL);
        validateTechStacks(techStacks);

        if (projectSkillRepository.existsByProject(project)) {
            projectSkillRepository.deleteAllByProject(project);
        }

        List<ProjectSkill> skills = convertAllToEntity(project, techStacks);
        projectSkillRepository.saveAll(skills);

        return skills.stream()
            .map(ProjectSkillSummary::from)
            .toList();
    }

    private List<ProjectSkill> convertAllToEntity(Project project, List<UpdateUserSkillRequest> techStacks) {
        return techStacks.stream().map(
            techStack -> {
                Skill skill = skillRepository.findById(techStack.skillId())
                    .orElseThrow(() -> new EntityNotFoundException(SKILL_NOT_EXISTING));

                return techStack.toProjectSkill(project, skill);
            }
        ).toList();
    }
}
