package sixgaezzang.sidepeek.projects.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSaveRequest;
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
    public ProjectResponse save(ProjectSaveRequest projectSaveRequest) {
        // TODO: accessToken에서 userId 꺼내서 ownerId와 비교!!

        Project project = projectSaveRequest.toEntity();
        projectRepository.save(project);

        // Required
        List<ProjectSkillSummary> techStacks = projectSkillService.saveAll(project, projectSaveRequest.techStacks());

        // Option
        List<MemberSummary> members = memberService.saveAll(project, projectSaveRequest.members());
        List<OverviewImageSummary> overviewImages =
            fileService.saveAll(project, projectSaveRequest.overviewImageUrls());

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

}
