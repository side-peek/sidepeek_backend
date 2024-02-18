package sixgaezzang.sidepeek.skill.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.skill.domain.Skill;

@Builder
public record SkillResponse(
    Long id,
    String name,
    String iconImageUrl
) {

    public static SkillResponse from(Skill skill) {
        return SkillResponse.builder()
            .id(skill.getId())
            .name(skill.getName())
            .iconImageUrl(skill.getIconImageUrl())
            .build();
    }

}
