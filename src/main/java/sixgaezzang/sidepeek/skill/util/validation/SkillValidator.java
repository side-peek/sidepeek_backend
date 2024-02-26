package sixgaezzang.sidepeek.skill.util.validation;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateBlank;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_CATEGORY_LENGTH;

import io.jsonwebtoken.lang.Assert;
import sixgaezzang.sidepeek.skill.domain.Skill;

public class SkillValidator {

    public static void validateSkill(Skill skill) {
        Assert.notNull(skill, "스킬이 null 입니다.");
    }

    public static void validateCategory(String category) {
        validateBlank(category, "스킬 카테고리를 입력해주세요.");
        validateMaxLength(category, MAX_CATEGORY_LENGTH,
            "스킬 카테고리는 " + MAX_CATEGORY_LENGTH + "자를 넘을 수 없습니다.");
    }

}
