package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.CATEGORY_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.TECH_STACKS_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.TECH_STACKS_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_ID_IS_NULL;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.common.util.ValidationUtils;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectSkillRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectSkillValidator {

    public static void validateTechStacks(List<SaveProjectSkillRequest> techStacks) {
        ValidationUtils.validateNotNullAndEmpty(techStacks, TECH_STACKS_IS_NULL);

        Assert.isTrue(techStacks.size() < MAX_TECH_STACK_COUNT, TECH_STACKS_OVER_MAX_COUNT);

        techStacks.forEach(ProjectSkillValidator::validateTechStack);
    }

    public static void validateTechStack(SaveProjectSkillRequest techStack) {
        Assert.notNull(techStack.skillId(), SKILL_ID_IS_NULL);
        Assert.notNull(techStack.category(), CATEGORY_IS_NULL);
    }
}
