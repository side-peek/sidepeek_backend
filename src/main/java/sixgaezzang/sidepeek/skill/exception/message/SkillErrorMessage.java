package sixgaezzang.sidepeek.skill.exception.message;

import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_CATEGORY_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkillErrorMessage {
    public static final String SKILL_NOT_EXISTING = "Skill Id에 해당하는 스킬이 없습니다.";
    public static final String SKILL_IS_NULL = "기술 스택이 null 입니다.";
    public static final String SKILL_ID_IS_NULL = "기술 스택의 스택 Id를 입력해주세요";

    // Category
    public static final String CATEGORY_OVER_MAX_LENGTH = "기술 스택 카테고리는 " + MAX_CATEGORY_LENGTH + "자 이하여야 합니다.";
    public static final String CATEGORY_IS_NULL = "기술 스택 카테고리를 입력해주세요.";
}
