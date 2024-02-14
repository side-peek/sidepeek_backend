package sixgaezzang.sidepeek.skill.dto;

import lombok.Builder;

@Builder
public record SkillResponse(
    Long id,
    String name,
    String iconImageUrl
) {
}
