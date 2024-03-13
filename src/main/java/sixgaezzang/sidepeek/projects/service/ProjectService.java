package sixgaezzang.sidepeek.projects.service;

import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginId;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginIdEqualsOwnerId;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_NOT_EXISTING;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.USER_PROJECT_SEARCH_TYPE_IS_INVALID;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.BANNER_PROJECT_COUNT;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateLoginIdEqualsUserId;

import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.comments.dto.response.CommentWithCountResponse;
import sixgaezzang.sidepeek.comments.service.CommentService;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;
import sixgaezzang.sidepeek.common.dto.response.Page;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;
import sixgaezzang.sidepeek.like.repository.LikeRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.UserProjectSearchType;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.request.SaveMemberRequest;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.request.UpdateProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.dto.response.ProjectBannerResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectSkillService projectSkillService;
    private final MemberService memberService;
    private final FileService fileService;
    private final LikeRepository likeRepository;
    private final CommentService commentService;

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

        CommentWithCountResponse comments = commentService.findAll(project);

        return ProjectResponse.from(project, overviewImages, techStacks, members, comments);
    }

    public List<ProjectBannerResponse> findAllPopularThisWeek() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = getStartDayOfWeek(endDate);

        return projectRepository.findAllPopularOfPeriod(startDate, endDate, BANNER_PROJECT_COUNT);
    }

    public Page<ProjectListResponse> findByUser(Long userId, Long loginId,
                                                UserProjectSearchType type, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXISTING));

        List<Long> likedProjectIds = getLikedProjectIds(userId);

        switch (type) {
            case JOINED:
                return findJoinedProjectsByUser(user, likedProjectIds, pageable);
            case LIKED:
                validateLoginIdEqualsUserId(loginId, userId);
                return findLikedProjectsByUser(user, likedProjectIds, pageable);
            case COMMENTED:
                validateLoginIdEqualsUserId(loginId, userId);
                return findAllByUserCommentedByUser(user, likedProjectIds, pageable);
            default:
                throw new IllegalArgumentException(USER_PROJECT_SEARCH_TYPE_IS_INVALID);
        }
    }

    public List<ProjectListResponse> findAll(Long userId, String sort, boolean isReleased) {
        List<Long> likedProjectIds = getLikedProjectIds(userId);

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
            .orElseThrow(
                () -> new InvalidAuthenticationException(ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE));
    }

    private List<Long> getLikedProjectIds(Long userId) {
        return Optional.ofNullable(userId)
            .map(likeRepository::findAllProjectIdsByUser)
            .orElse(Collections.emptyList());
    }

    private Page<ProjectListResponse> findJoinedProjectsByUser(User user,
                                                               List<Long> likedProjectIds,
                                                               Pageable pageable) {
        return Page.from(projectRepository.findAllByUserJoined(likedProjectIds, user, pageable));
    }

    private Page<ProjectListResponse> findLikedProjectsByUser(User user, List<Long> likedProjectIds,
                                                              Pageable pageable) {
        return Page.from(projectRepository.findAllByUserLiked(likedProjectIds, user, pageable));
    }

    private Page<ProjectListResponse> findAllByUserCommentedByUser(User user,
                                                                   List<Long> likedProjectIds,
                                                                   Pageable pageable) {
        return Page.from(projectRepository.findAllByUserCommented(likedProjectIds, user, pageable));
    }

    private LocalDate getStartDayOfWeek(LocalDate endWeekDay) {
        return endWeekDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
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
