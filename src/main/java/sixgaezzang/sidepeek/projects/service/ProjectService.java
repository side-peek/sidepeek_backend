package sixgaezzang.sidepeek.projects.service;

import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.OWNER_ID_NOT_EQUALS_LOGIN_ID;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginId;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateOwnerId;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.comments.dto.response.CommentResponse;
import sixgaezzang.sidepeek.comments.service.CommentService;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;
import sixgaezzang.sidepeek.like.repository.LikeRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.exception.ProjectErrorCode;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final LikeRepository likeRepository;
    private final ProjectSkillService projectSkillService;
    private final MemberService memberService;
    private final FileService fileService;
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
                    () -> new EntityNotFoundException(
                        ProjectErrorCode.ID_NOT_EXISTING.getMessage()));
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
                () -> new EntityNotFoundException(ProjectErrorCode.ID_NOT_EXISTING.getMessage()));

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

    @Transactional
    public void delete(Long loginId, Long projectId) {
        validateLoginId(loginId);

        Project project = projectRepository.findById(projectId)
            .orElseThrow(
                () -> new EntityNotFoundException(ProjectErrorCode.ID_NOT_EXISTING.getMessage()));
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

}
