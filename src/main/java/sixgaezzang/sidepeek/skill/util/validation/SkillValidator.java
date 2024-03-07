package sixgaezzang.sidepeek.skill.util.validation;

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

}
