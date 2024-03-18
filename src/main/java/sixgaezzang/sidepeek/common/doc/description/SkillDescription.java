package sixgaezzang.sidepeek.common.doc.description;

import static sixgaezzang.sidepeek.skill.domain.Skill.MAX_SKILL_NAME_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SkillDescription {
    public static final String SKILL_KEYWORD_DESCRIPTION = "검색어, " + MAX_SKILL_NAME_LENGTH + "자 이하";
}
