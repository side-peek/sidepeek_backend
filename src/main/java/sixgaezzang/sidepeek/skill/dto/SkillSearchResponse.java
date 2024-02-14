package sixgaezzang.sidepeek.skill.dto;

import java.util.List;
import sixgaezzang.sidepeek.skill.domain.Skill;

public record SkillSearchResponse(
    List<Skill> skills
) {
}
