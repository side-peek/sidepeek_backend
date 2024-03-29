package sixgaezzang.sidepeek.common.dto.request;

import static sixgaezzang.sidepeek.common.doc.description.CommonDescription.TECH_STACK_CATEGORY_DESCRIPTION;
import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.CATEGORY_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.CATEGORY_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_CATEGORY_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_ID_IS_NULL;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.domain.UserSkill;

@Schema(description = "기술 스택 저장 요청 정보")
public record SaveTechStackRequest(
    @Schema(description = "기술 스택 Id", example = "1")
    @Min(value = MIN_ID, message = "스킬 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    @NotNull(message = SKILL_ID_IS_NULL)
    Long skillId,

    @Schema(description = TECH_STACK_CATEGORY_DESCRIPTION, example = "프론트엔드")
    @Size(max = MAX_CATEGORY_LENGTH, message = CATEGORY_OVER_MAX_LENGTH)
    @NotBlank(message = CATEGORY_IS_NULL)
    String category
) {

    public UserSkill toUserSkill(User user, Skill skill) {
        return UserSkill.builder()
            .user(user)
            .skill(skill)
            .category(this.category)
            .build();
    }

    public ProjectSkill toProjectSkill(Project project, Skill skill) {
        return ProjectSkill.builder()
            .project(project)
            .skill(skill)
            .category(this.category)
            .build();
    }

}
