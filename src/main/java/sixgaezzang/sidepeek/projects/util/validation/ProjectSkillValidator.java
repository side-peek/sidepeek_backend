package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_SKILL_COUNT;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import sixgaezzang.sidepeek.common.util.ValidationUtils;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;

public class ProjectSkillValidator {

    public static void validateTechStacks(List<ProjectSkillSaveRequest> techStacks) {
        ValidationUtils.validateNotNullAndEmpty(techStacks,
            "기술 스택들을 입력해주세요.");

        Assert.isTrue(techStacks.size() < MAX_PROJECT_SKILL_COUNT,
            "기술 스택은 " + MAX_PROJECT_SKILL_COUNT + "개 미만이어야 합니다.");

        techStacks.forEach(ProjectSkillValidator::validateTechStack);
    }

    public static void validateTechStack(ProjectSkillSaveRequest techStack) {
        Assert.notNull(techStack.skillId(), "기술 스택의 스택 Id를 입력해주세요");
        Assert.notNull(techStack.category(), "기술 스택 카테고리를 입력해주세요.");
    }
}
