package sixgaezzang.sidepeek.projects.service;

import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.OWNER_ID_NOT_EQUALS_LOGIN_ID;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginId;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_NOT_EXISTING;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.USER_PROJECT_SEARCH_TYPE_IS_INVALID;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateOwnerId;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateLoginIdEqualsUserId;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.comments.dto.response.CommentWithCountResponse;
import sixgaezzang.sidepeek.comments.service.CommentService;
import sixgaezzang.sidepeek.common.dto.response.Page;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;
import sixgaezzang.sidepeek.like.repository.LikeRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.UserProjectSearchType;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
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
    public ProjectResponse save(Long loginId, Long projectId, SaveProjectRequest request) {
        validateLoginId(loginId);

        Project project;
        if (Objects.isNull(projectId)) {
            validateLoginIdEqualsOwnerId(loginId, request.ownerId());

            project = request.toEntity();
            projectRepository.save(project);
        } else {
            project = projectRepository.findById(projectId)
                .orElseThrow(
                    () -> new EntityNotFoundException(PROJECT_NOT_EXISTING));
            validateLoginUserIncludeMembers(loginId, project);

            project.update(request);
        }

        List<ProjectSkillSummary> techStacks = projectSkillService.saveAll(project,
            request.techStacks());
        List<MemberSummary> members = memberService.saveAll(project, request.members());
        List<OverviewImageSummary> overviewImages = fileService.saveAll(project,
            request.overviewImageUrls());

        return ProjectResponse.from(project, overviewImages, techStacks, members);
    }

    public List<ProjectListResponse> findAll(Long userId, String sort, boolean isReleased) {
        List<Long> likedProjectIds = getLikedProjectIds(userId);

        return projectRepository.findAllBySortAndStatus(likedProjectIds, sort, isReleased);
    }

    @Transactional
    public ProjectResponse findById(Long id) {

        Project project = projectRepository.findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException(PROJECT_NOT_EXISTING));

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

    @Transactional
    public void delete(Long loginId, Long projectId) {
        validateLoginId(loginId);

        Project project = projectRepository.findById(projectId)
            .orElseThrow(
                () -> new EntityNotFoundException(PROJECT_NOT_EXISTING));
        validateLoginIdEqualsOwnerId(loginId, project.getOwnerId());

        project.softDelete();
    }

    private void validateLoginIdEqualsOwnerId(Long loginId, Long ownerId) {
        validateOwnerId(ownerId);
        if (!loginId.equals(ownerId)) {
            throw new InvalidAuthenticationException(OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }
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

}
