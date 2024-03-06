package sixgaezzang.sidepeek.skill.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import sixgaezzang.sidepeek.skill.domain.Skill;

@Schema(description = "기술 스택 정보")
@Builder
public record SkillResponse(
    @Schema(description = "기술 스택 식별자", example = "1")
    Long id,
    @Schema(description = "기술 스택 이름", example = "React")
    String name,
    @Schema(description = "기술 스택 아이콘 이미지 URL", example = "https://sidepeek.image/icon1.jpg")
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
