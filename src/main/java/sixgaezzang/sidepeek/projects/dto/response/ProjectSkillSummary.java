package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import sixgaezzang.sidepeek.skill.dto.response.SkillResponse;

@Schema(description = "프로젝트 기술 스택 정보")
@Builder
public record ProjectSkillSummary(
    @Schema(description = "프로젝트 기술 스택 카테고리", example = "프론트엔드")
    String category,
    @Schema(description = "프로젝트 기술 스택 상세 정보")
    List<SkillResponse> skill
) {

    public static ProjectSkillSummary of(String category, List<SkillResponse> skill) {
        return ProjectSkillSummary.builder()
            .category(category)
            .skill(skill)
            .build();
    }

}
