package sixgaezzang.sidepeek.skill.util.validation;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_CATEGORY_LENGTH;

import io.jsonwebtoken.lang.Assert;
import sixgaezzang.sidepeek.skill.domain.Skill;

public class SkillValidator {

    public static void validateSkill(Skill skill) {
        Assert.notNull(skill, "기술 스택이 null 입니다.");
    }

    public static void validateCategory(String category) {
        validateNotBlank(category, "기술 스택 카테고리를 입력해주세요.");
        validateMaxLength(category, MAX_CATEGORY_LENGTH,
            "기술 스택 카테고리는 " + MAX_CATEGORY_LENGTH + "자 미만이어야 합니다.");
    }

}
