package sixgaezzang.sidepeek.projects.service;

import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_SKILL_COUNT;

import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.common.util.ValidationUtils;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;
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

    @Transactional
    public List<ProjectSkillSummary> saveAll(Project project, List<ProjectSkillSaveRequest> techStacks) {
        validateTechStacks(techStacks);

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

    private void validateTechStacks(List<ProjectSkillSaveRequest> techStacks) {
        ValidationUtils.validateNotNullAndEmpty(techStacks,
            "기술 스택들을 입력해주세요.");
        Assert.isTrue(techStacks.size() <= MAX_PROJECT_SKILL_COUNT,
            "기술 스택은 " + MAX_PROJECT_SKILL_COUNT + "개를 넘을 수 없습니다.");
    }
}
