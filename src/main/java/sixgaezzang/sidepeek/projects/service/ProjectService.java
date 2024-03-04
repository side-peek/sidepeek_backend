package sixgaezzang.sidepeek.projects.service;

import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.OWNER_ID_NOT_EQUALS_LOGIN_ID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.request.ProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.exception.ProjectErrorCode;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectSkillService projectSkillService;
    private final MemberService memberService;
    private final FileService fileService;

    @Transactional
    public ProjectResponse save(Long loginId, ProjectRequest request) {
        validateLoginIdEqualsOwnerId(loginId, request.ownerId());

        Project project = request.toEntity();
        projectRepository.save(project);

        List<ProjectSkillSummary> techStacks = projectSkillService.saveAll(project, request.techStacks());
        List<MemberSummary> members = memberService.saveAll(project, request.members());
        List<OverviewImageSummary> overviewImages =
            fileService.saveAll(project, request.overviewImageUrls());

        return ProjectResponse.from(project, overviewImages, techStacks, members);
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

        return ProjectResponse.from(project, overviewImages, techStacks, members);
    }

    @Transactional
    public ProjectResponse update(Long loginId, Long projectId, ProjectRequest request) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(
                () -> new EntityNotFoundException(ProjectErrorCode.ID_NOT_EXISTING.getMessage()));

        memberService.findFellowMemberByProject(loginId, project)
            .orElseThrow(() -> new InvalidAuthenticationException(ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE));

        Project updatedProject = project.update(request);

        List<ProjectSkillSummary> techStacks = projectSkillService.saveAll(updatedProject, request.techStacks());
        List<MemberSummary> members = memberService.saveAll(updatedProject, request.members());
        List<OverviewImageSummary> overviewImages =
            fileService.saveAll(updatedProject, request.overviewImageUrls());

        return ProjectResponse.from(updatedProject, overviewImages, techStacks, members);
    }

    @Transactional
    public void delete(Long loginId, Long projectId) {
        // TODO: 생성, 수정할 땐 ownerId와 loginId를 비교하는 로직이 있다. 여기에도 적용하는 것이 좋을까?
        Project project = projectRepository.findById(projectId)
            .orElseThrow(
                () -> new EntityNotFoundException(ProjectErrorCode.ID_NOT_EXISTING.getMessage()));
        validateLoginIdEqualsOwnerId(loginId, project.getOwnerId());

        project.setDeletedAt(LocalDateTime.now()); // TODO: 타임존 설정이 필요할까
    }

    private void validateLoginIdEqualsOwnerId(Long loginId, Long ownerId) {
        if (!loginId.equals(ownerId)) {
            throw new InvalidAuthenticationException(OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }
    }

}
