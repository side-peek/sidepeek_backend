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
    }
}
