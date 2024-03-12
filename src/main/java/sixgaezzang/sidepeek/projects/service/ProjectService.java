package sixgaezzang.sidepeek.projects.service;

import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginId;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginIdEqualsOwnerId;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_NOT_EXISTING;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.comments.dto.response.CommentResponse;
import sixgaezzang.sidepeek.comments.service.CommentService;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;
import sixgaezzang.sidepeek.common.exception.InvalidAuthorityException;
import sixgaezzang.sidepeek.like.repository.LikeRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.request.SaveMemberRequest;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.request.UpdateProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectSkillService projectSkillService;
    private final MemberService memberService;
    private final FileService fileService;
    private final CommentService commentService;
    private final ProjectRepository projectRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public ProjectResponse save(Long loginId, SaveProjectRequest request) {
        validateLoginId(loginId);
        validateLoginIdEqualsOwnerId(loginId, request.ownerId());

        Project project = request.toEntity();
        projectRepository.save(project);

        return GetProjectResponseAfterSaveLists(project, request.techStacks(), request.members(),
            request.overviewImageUrls());
    }

    public Project getById(Long projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXISTING));
    }

    @Transactional
    public ProjectResponse findById(Long id) {

        Project project = getById(id);

        project.increaseViewCount();

        List<OverviewImageSummary> overviewImages = fileService.findAllByType(
                project, FileType.OVERVIEW_IMAGE)
            .stream()
            .map(OverviewImageSummary::from)
            .toList();

        List<ProjectSkillSummary> techStacks = projectSkillService.findAll(project)
            .stream()
            .map(ProjectSkillSummary::from)
            .toList();

        List<MemberSummary> members = memberService.findAllWithUser(project);

        List<CommentResponse> comments = commentService.findAll(project);

        return ProjectResponse.from(project, overviewImages, techStacks, members, comments);
    }

    public List<ProjectListResponse> findAll(Long userId, String sort, boolean isReleased) {
        List<Long> likedProjectIds =
            (userId != null) ? likeRepository.findAllProjectIdsByUser(userId)
                : Collections.emptyList();

        return projectRepository.findAllBySortAndStatus(likedProjectIds, sort, isReleased);
    }

    @Transactional
    public ProjectResponse update(Long loginId, Long projectId, UpdateProjectRequest request) {
        validateLoginId(loginId);

        Project project = getById(projectId);
        validateLoginUserIncludeMembers(loginId, project);

        project.update(request);

        return GetProjectResponseAfterSaveLists(project, request.techStacks(), request.members(),
            request.overviewImageUrls());
    }

    @Transactional
    public void delete(Long loginId, Long projectId) {
        validateLoginId(loginId);

        Project project = getById(projectId);
        validateLoginIdEqualsOwnerId(loginId, project.getOwnerId());

        project.softDelete();
    }

    private void validateLoginUserIncludeMembers(Long loginId, Project project) {
        memberService.findFellowMemberByProject(loginId, project)
            .orElseThrow(() -> new InvalidAuthorityException(ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE));
    }

    private ProjectResponse GetProjectResponseAfterSaveLists(Project project, List<SaveTechStackRequest> request,
                                                             List<SaveMemberRequest> request1,
                                                             List<String> request2) {
        List<ProjectSkillSummary> techStacks = projectSkillService.cleanAndSaveAll(project, request);
        List<MemberSummary> members = memberService.cleanAndSaveAll(project, request1);
        List<OverviewImageSummary> overviewImages = fileService.cleanAndSaveAll(project, request2);

        return ProjectResponse.from(project, overviewImages, techStacks, members);
    }

}
