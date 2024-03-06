package sixgaezzang.sidepeek.skill.util.validation;

import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.CATEGORY_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.CATEGORY_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_CATEGORY_LENGTH;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_IS_NULL;

import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.skill.domain.Skill;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkillValidator {

    public static void validateSkill(Skill skill) {
        Assert.notNull(skill, SKILL_IS_NULL);
    }

    public static void validateCategory(String category) {
        validateNotBlank(category, CATEGORY_IS_NULL);
        validateMaxLength(category, MAX_CATEGORY_LENGTH,
            CATEGORY_OVER_MAX_LENGTH);
    }

}
