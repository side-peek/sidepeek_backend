package sixgaezzang.sidepeek.projects.exception.message;

import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_SKILL_COUNT;

public class ProjectSkillErrorMessage {

    public static final String PROJECT_TECH_STACKS_IS_NULL = "기술 스택들을 입력해주세요.";
    public static final String PROJECT_TECH_STACKS_OVER_MAX_COUNT =
        "기술 스택은 " + MAX_PROJECT_SKILL_COUNT + "개 미만이어야 합니다.";
}
