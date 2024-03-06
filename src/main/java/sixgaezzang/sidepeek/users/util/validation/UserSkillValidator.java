package sixgaezzang.sidepeek.users.util.validation;

import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.TECH_STACKS_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.TECH_STACKS_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.CATEGORY_IS_NULL;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_ID_IS_NULL;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.common.util.ValidationUtils;
import sixgaezzang.sidepeek.users.dto.request.UpdateUserSkillRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSkillValidator {
    public static void validateTechStacks(List<UpdateUserSkillRequest> techStacks) {
        ValidationUtils.validateNotNullAndEmpty(techStacks, TECH_STACKS_IS_NULL);

        Assert.isTrue(techStacks.size() < MAX_TECH_STACK_COUNT, TECH_STACKS_OVER_MAX_COUNT);

        techStacks.forEach(UserSkillValidator::validateTechStack);
    }

    public static void validateTechStack(UpdateUserSkillRequest techStack) {
        Assert.notNull(techStack.skillId(), SKILL_ID_IS_NULL);
        Assert.notNull(techStack.category(), CATEGORY_IS_NULL);
    }
}
