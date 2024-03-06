package sixgaezzang.sidepeek.projects.util;

import static sixgaezzang.sidepeek.projects.util.FakeValueProvider.createNickname;
import static sixgaezzang.sidepeek.projects.util.FakeValueProvider.createRole;

import java.time.YearMonth;
import java.util.List;
import sixgaezzang.sidepeek.projects.dto.request.SaveMemberRequest;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectSkillRequest;

public class FakeDtoProvider {

    // Project Skill
    public static SaveProjectSkillRequest createProjectSkillSaveRequest(Long skillId) {
        return new SaveProjectSkillRequest(skillId, FakeValueProvider.createSkillCategory());
    }

    // Project
    public static SaveProjectRequest createProjectSaveRequestOnlyRequired(
        String name, String overview, String githubUrl, String description, Long ownerId,
        List<SaveProjectSkillRequest> techStacks, List<SaveMemberRequest> members
    ) {
        return new SaveProjectRequest(name, overview, ownerId, githubUrl, description,
            techStacks, null, null, null, null,
            null, null, null, members);
    }

    public static SaveProjectRequest createProjectSaveRequestWithOwnerIdAndOption(
        List<SaveProjectSkillRequest> techStacks, Long ownerId, String subName, String thumbnailUrl, String deployUrl,
        String troubleShooting, YearMonth startDate, YearMonth endDate
    ) {
        return new SaveProjectRequest(
            FakeValueProvider.createProjectName(), FakeValueProvider.createOverview(), ownerId,
            FakeValueProvider.createUrl(), FakeValueProvider.createLongText(), techStacks, subName,
            thumbnailUrl, deployUrl, startDate, endDate, troubleShooting, null, null
        );
    }

    // Member
    public static SaveMemberRequest createFellowMemberSaveRequest(Long userId) {
        return new SaveMemberRequest(userId, null, createRole());
    }

    public static SaveMemberRequest createNonFellowMemberSaveRequest() {
        return new SaveMemberRequest(null, createNickname(), createRole());
    }
}
