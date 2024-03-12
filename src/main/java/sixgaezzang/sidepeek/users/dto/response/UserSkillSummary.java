package sixgaezzang.sidepeek.users.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import sixgaezzang.sidepeek.skill.dto.response.SkillResponse;
import sixgaezzang.sidepeek.users.domain.UserSkill;

@Schema(description = "회원 기술 스택 정보")
@Builder
public record UserSkillSummary(
    @Schema(description = "회원 기술 스택 식별자", example = "1")
    Long id,

    @Schema(description = "회원 기술 스택 카테고리", example = "프론트엔드")
    String category,

    @Schema(description = "기술 스택 상세 정보")
    SkillResponse skill
) {

    public static UserSkillSummary from(UserSkill userSkill) {
        return UserSkillSummary.builder()
            .id(userSkill.getId())
            .category(userSkill.getCategory())
            .skill(SkillResponse.from(userSkill.getSkill()))
            .build();
    }

}
