package sixgaezzang.sidepeek.skill.exception.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SkillErrorMessage {
    public static final String SKILL_NOT_EXISTING = "Skill Id에 해당하는 스킬이 없습니다.";
    public static final String SKILL_IS_NULL = "기술 스택이 null 입니다.";
    public static final String SKILL_ID_IS_NULL = "기술 스택의 스택 Id를 입력해주세요";
}
