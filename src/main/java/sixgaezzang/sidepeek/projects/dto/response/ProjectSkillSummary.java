package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.skill.dto.response.SkillResponse;

@Schema(description = "프로젝트 기술 스택 정보")
@Builder
public record ProjectSkillSummary(
    @Schema(description = "프로젝트 기술 스택 식별자", example = "1")
    Long id,
    @Schema(description = "프로젝트 기술 스택 카테고리", example = "프론트엔드")
    String category,
    @Schema(description = "프로젝트 기술 스택 상세 정보")
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
