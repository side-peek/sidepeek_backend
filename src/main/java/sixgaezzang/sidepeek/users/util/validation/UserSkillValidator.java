package sixgaezzang.sidepeek.users.util.validation;

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
import sixgaezzang.sidepeek.users.dto.request.UpdateUserSkillRequest;

// TODO: ProjectSkillValidator와 로직이 상당히 유사해서 추후 리팩터링 예정
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSkillValidator {
    public static void validateUserTechStacks(List<UpdateUserSkillRequest> techStacks) {
        ValidationUtils.validateNotNullAndEmpty(techStacks, TECH_STACKS_IS_NULL);

        Assert.isTrue(techStacks.size() <= MAX_TECH_STACK_COUNT, TECH_STACKS_OVER_MAX_COUNT);

        techStacks.forEach(UserSkillValidator::validateUserTechStack);
    }

    public static void validateUserTechStack(UpdateUserSkillRequest techStack) {
        Assert.notNull(techStack.skillId(), SKILL_ID_IS_NULL);
        Assert.notNull(techStack.category(), CATEGORY_IS_NULL);
    }
}
