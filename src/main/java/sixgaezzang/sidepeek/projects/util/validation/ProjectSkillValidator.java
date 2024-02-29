package sixgaezzang.sidepeek.projects.util.validation;

import static sixgaezzang.sidepeek.projects.exception.ProjectSkillErrorMessage.CATEGORY_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.ProjectSkillErrorMessage.PROJECT_TECH_STACKS_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.ProjectSkillErrorMessage.PROJECT_TECH_STACKS_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.exception.ProjectSkillErrorMessage.SKILL_ID_IS_NULL;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_SKILL_COUNT;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import sixgaezzang.sidepeek.common.util.ValidationUtils;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;

public class ProjectSkillValidator {

    public static void validateTechStacks(List<ProjectSkillSaveRequest> techStacks) {
        ValidationUtils.validateNotNullAndEmpty(techStacks, PROJECT_TECH_STACKS_IS_NULL);

        Assert.isTrue(techStacks.size() < MAX_PROJECT_SKILL_COUNT, PROJECT_TECH_STACKS_OVER_MAX_COUNT);

        techStacks.forEach(ProjectSkillValidator::validateTechStack);
    }

    public static void validateTechStack(ProjectSkillSaveRequest techStack) {
        Assert.notNull(techStack.skillId(), SKILL_ID_IS_NULL);
        Assert.notNull(techStack.category(), CATEGORY_IS_NULL);
    }
}
