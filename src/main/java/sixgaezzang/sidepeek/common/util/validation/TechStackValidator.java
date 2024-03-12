package sixgaezzang.sidepeek.common.util.validation;

import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.CATEGORY_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.CATEGORY_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.TECH_STACKS_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.TECH_STACK_IS_DUPLICATED;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_CATEGORY_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateCollection;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateDuplicate;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_ID_IS_NULL;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TechStackValidator {

    public static void validateTechStacks(List<SaveTechStackRequest> techStacks) {
        Assert.isTrue(techStacks.size() <= MAX_TECH_STACK_COUNT, TECH_STACKS_OVER_MAX_COUNT);
        validateCollection(techStacks, TechStackValidator::validateTechStack);
        validateDuplicate(techStacks, TECH_STACK_IS_DUPLICATED);
    }

    public static void validateTechStack(SaveTechStackRequest techStack) {
        Assert.notNull(techStack.skillId(), SKILL_ID_IS_NULL);
        Assert.notNull(techStack.category(), CATEGORY_IS_NULL);
    }

    public static void validateCategory(String category) {
        validateNotBlank(category, CATEGORY_IS_NULL);
        validateMaxLength(category, MAX_CATEGORY_LENGTH, CATEGORY_OVER_MAX_LENGTH);
    }

}
