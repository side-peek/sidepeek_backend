package sixgaezzang.sidepeek.skill.dto;

import java.util.List;
import sixgaezzang.sidepeek.skill.domain.Skill;

public record SkillSearchResponse(
    List<SkillResponse> skills
) {

    public static SkillSearchResponse from(List<Skill> skills) {
        List<SkillResponse> skillResponses = skills.stream()
            .map(SkillResponse::from)
            .toList();

        return new SkillSearchResponse(skillResponses);
    }

}
