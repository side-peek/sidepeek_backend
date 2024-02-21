package sixgaezzang.sidepeek.projects.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.skill.dto.response.SkillResponse;

@Builder
public record ProjectSkillSummary(
    Long id,
    String category,
    SkillResponse skill
) {

    public static ProjectSkillSummary from(ProjectSkill projectSkill) {
        return ProjectSkillSummary.builder()
            .id(projectSkill.getId())
            .category(projectSkill.getCategory())
            .skill(SkillResponse.from(projectSkill.getSkill()))
            .build();
    }

}
