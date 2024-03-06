package sixgaezzang.sidepeek.projects.util;

import static sixgaezzang.sidepeek.projects.util.FakeValueProvider.createNickname;
import static sixgaezzang.sidepeek.projects.util.FakeValueProvider.createRole;

import java.time.YearMonth;
import java.util.List;
import sixgaezzang.sidepeek.projects.dto.request.MemberSaveRequest;
import sixgaezzang.sidepeek.projects.dto.request.ProjectRequest;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;

public class FakeDtoProvider {

    // Project Skill
    public static ProjectSkillSaveRequest createProjectSkillSaveRequest(Long skillId) {
        return new ProjectSkillSaveRequest(skillId, FakeValueProvider.createSkillCategory());
    }

    // Project
    public static ProjectRequest createProjectSaveRequestOnlyRequired(
        String name, String overview, String githubUrl, String description, Long ownerId,
        List<ProjectSkillSaveRequest> techStacks, List<MemberSaveRequest> members
    ) {
        return new ProjectRequest(name, overview, ownerId, githubUrl, description,
            techStacks, null, null, null, null,
            null, null, null, members);
    }

    public static ProjectRequest createProjectSaveRequestWithOwnerIdAndOption(
        List<ProjectSkillSaveRequest> techStacks, Long ownerId, String subName, String thumbnailUrl, String deployUrl,
        String troubleShooting, YearMonth startDate, YearMonth endDate
    ) {
        return new ProjectRequest(
            FakeValueProvider.createProjectName(), FakeValueProvider.createOverview(), ownerId,
            FakeValueProvider.createUrl(), FakeValueProvider.createLongText(), techStacks, subName,
            thumbnailUrl, deployUrl, startDate, endDate, troubleShooting, null, null
        );
    }

    // Member
    public static MemberSaveRequest createFellowMemberSaveRequest(Long userId) {
        return new MemberSaveRequest(userId, null, createRole());
    }

    public static MemberSaveRequest createNonFellowMemberSaveRequest() {
        return new MemberSaveRequest(null, createNickname(), createRole());
    }
}
