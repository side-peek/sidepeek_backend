package sixgaezzang.sidepeek.projects.exception;

import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_SKILL_COUNT;

public class ProjectSkillErrorMessage {

    public static final String PROJECT_TECH_STACKS_IS_NULL = "기술 스택들을 입력해주세요.";
    public static final String PROJECT_TECH_STACKS_OVER_MAX_COUNT =
        "기술 스택은 " + MAX_PROJECT_SKILL_COUNT + "개 미만이어야 합니다.";
    public static final String SKILL_ID_IS_NULL = "기술 스택의 스택 Id를 입력해주세요";
    public static final String CATEGORY_IS_NULL = "기술 스택 카테고리를 입력해주세요.";

}
