package sixgaezzang.sidepeek.util;

import static sixgaezzang.sidepeek.util.FakeValueProvider.createLongText;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createNickname;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createOverview;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createProjectName;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createRole;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createSkillCategory;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createUrl;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;
import sixgaezzang.sidepeek.projects.dto.request.SaveMemberRequest;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.users.domain.Career;
import sixgaezzang.sidepeek.users.domain.Job;
import sixgaezzang.sidepeek.users.dto.request.UpdateUserProfileRequest;

public class FakeDtoProvider {

    // TechStack
    public static SaveTechStackRequest createSaveTechStackRequest(Long skillId) {
        return new SaveTechStackRequest(skillId, createSkillCategory());
    }

    // Project
    public static SaveProjectRequest createProjectSaveRequestOnlyRequired(
        String name, String overview, String githubUrl, String description, Long ownerId,
        List<SaveTechStackRequest> techStacks, List<SaveMemberRequest> members
    ) {
        return new SaveProjectRequest(name, overview, ownerId, githubUrl, description,
            techStacks, null, null, null, null,
            null, null, null, members);
    }

    public static SaveProjectRequest createSaveProjectRequestWithOwnerIdAndOption(
        List<SaveTechStackRequest> techStacks, Long ownerId, String subName, String thumbnailUrl, String deployUrl,
        String troubleShooting, YearMonth startDate, YearMonth endDate
    ) {
        return new SaveProjectRequest(
            createProjectName(), createOverview(), ownerId,
            createUrl(), createLongText(), techStacks, subName,
            thumbnailUrl, deployUrl, startDate, endDate, troubleShooting, null, null
        );
    }

    // Member
    public static SaveMemberRequest createFellowSaveMemberRequest(Long userId) {
        return new SaveMemberRequest(userId, createNickname(), createRole());
    }

    public static SaveMemberRequest createNonFellowSaveMemberRequest() {
        return new SaveMemberRequest(null, createNickname(), createRole());
    }

    public static UpdateUserProfileRequest createUpdateUserProfileRequestWithEmptyTechStacks() {
        return new UpdateUserProfileRequest(
            FakeValueProvider.createNickname(),
            FakeValueProvider.createUrl(),
            FakeValueProvider.createIntroduction(),
            Job.BACKEND_DEVELOPER.getName(),
            Career.JUNIOR.getDescription(),
            FakeValueProvider.createUrl(),
            FakeValueProvider.createUrl(),
            Collections.emptyList()
        );
    }
}
