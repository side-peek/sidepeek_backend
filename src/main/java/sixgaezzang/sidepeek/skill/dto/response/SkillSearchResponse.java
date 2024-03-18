package sixgaezzang.sidepeek.skill.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import sixgaezzang.sidepeek.skill.domain.Skill;

@Schema(description = "기술 스택 검색 결과")
public record SkillSearchResponse(
    @Schema(description = "기술 스택 목록, 없으면 빈 배열")
    List<SkillResponse> skills
) {

    public static SkillSearchResponse from(List<Skill> skills) {
        List<SkillResponse> skillResponses = skills.stream()
            .map(SkillResponse::from)
            .toList();

        return new SkillSearchResponse(skillResponses);
    }

}
