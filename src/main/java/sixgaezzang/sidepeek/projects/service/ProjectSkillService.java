package sixgaezzang.sidepeek.projects.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static sixgaezzang.sidepeek.common.util.validation.TechStackValidator.validateTechStacks;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateProject;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;
import sixgaezzang.sidepeek.skill.dto.response.SkillResponse;
import sixgaezzang.sidepeek.skill.serivce.SkillService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectSkillService {

    private final ProjectSkillRepository projectSkillRepository;
    private final SkillService skillService;

    public List<ProjectSkillSummary> findAll(Project project) {
        List<ProjectSkill> skills = projectSkillRepository.findAllByProject(project);

        return generateProjectSkillSummaries(skills);
    }

    @Transactional
    public List<ProjectSkillSummary> cleanAndSaveAll(Project project,
        List<SaveTechStackRequest> techStacks) {
        validateProject(project);
        validateTechStacks(techStacks);

        cleanExistingProjectSkillsByProject(project);

        List<ProjectSkill> skills = convertAllToEntity(project, techStacks);

        return generateProjectSkillSummaries(projectSkillRepository.saveAll(skills));
    }

    private void cleanExistingProjectSkillsByProject(Project project) {
        if (projectSkillRepository.existsByProject(project)) {
            projectSkillRepository.deleteAllByProject(project);
        }
    }

    private List<ProjectSkill> convertAllToEntity(Project project,
        List<SaveTechStackRequest> techStacks) {
        return techStacks.stream()
            .map(techStack -> techStack.toProjectSkill(
                project,
                skillService.getById(techStack.skillId()))
            )
            .toList();
    }

    private List<ProjectSkillSummary> generateProjectSkillSummaries(List<ProjectSkill> skills) {
        Map<String, List<SkillResponse>> techStack = skills.stream()
            .collect(groupingBy(ProjectSkill::getCategory,
                mapping(skill -> SkillResponse.from(skill.getSkill()),
                    toList())));

        return techStack.entrySet().stream()
            .map(entry -> ProjectSkillSummary.of(entry.getKey(), entry.getValue()))
            .toList();
    }

}
