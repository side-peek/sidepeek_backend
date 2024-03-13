package sixgaezzang.sidepeek.projects.service;

import static sixgaezzang.sidepeek.common.util.validation.TechStackValidator.validateTechStacks;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateProject;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;
import sixgaezzang.sidepeek.skill.serivce.SkillService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectSkillService {

    private final ProjectSkillRepository projectSkillRepository;
    private final SkillService skillService;

    public List<ProjectSkill> findAll(Project project) {
        return projectSkillRepository.findAllByProject(project);
    }

    @Transactional
    public List<ProjectSkillSummary> cleanAndSaveAll(Project project, List<SaveTechStackRequest> techStacks) {
        validateProject(project);
        validateTechStacks(techStacks);

        cleanExistingProjectSkillsByProject(project);

        List<ProjectSkill> skills = convertAllToEntity(project, techStacks);

        return projectSkillRepository.saveAll(skills)
            .stream()
            .map(ProjectSkillSummary::from)
            .toList();
    }

    private void cleanExistingProjectSkillsByProject(Project project) {
        if (projectSkillRepository.existsByProject(project)) {
            projectSkillRepository.deleteAllByProject(project);
        }
    }

    private List<ProjectSkill> convertAllToEntity(Project project, List<SaveTechStackRequest> techStacks) {
        return techStacks.stream()
            .map(techStack -> techStack.toProjectSkill(
                project,
                skillService.getById(techStack.skillId()))
            )
            .toList();
    }

}
