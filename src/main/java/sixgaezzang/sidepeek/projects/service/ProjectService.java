package sixgaezzang.sidepeek.projects.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.exception.ProjectErrorCode;
import sixgaezzang.sidepeek.projects.repository.FileRepository;
import sixgaezzang.sidepeek.projects.repository.MemberRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    public ProjectResponse findById(Long id) {

        Project project = projectRepository.findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException(ProjectErrorCode.ID_NOT_EXISTING.getMessage()));

        project.increaseViewCount();

        List<OverviewImageSummary> overviewImages = fileRepository.findAllByProjectAndType(
                project, FileType.OVERVIEW_IMAGE)
            .stream()
            .map(OverviewImageSummary::from)
            .toList();

        List<ProjectSkillSummary> techStacks = projectSkillRepository.findAllByProject(project)
            .stream()
            .map(ProjectSkillSummary::from)
            .toList();

        List<MemberSummary> members = memberRepository.findAllByProject(project)
            .stream()
            .map(MemberSummary::from)
            .toList();

        return ProjectResponse.from(project, overviewImages, techStacks, members);
    }

}
