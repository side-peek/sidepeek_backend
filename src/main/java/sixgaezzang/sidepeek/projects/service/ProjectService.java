package sixgaezzang.sidepeek.projects.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.domain.member.Member;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.repository.FileRepository;
import sixgaezzang.sidepeek.projects.repository.MemberRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.UserSummaryResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    public ProjectResponse findById(Long id) {

        // 프로젝트 가져오기
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 존재하지 않습니다."));

        // 조회수 + 1
        project.increaseViewCount();

        // 프로젝트 개요에 사용될 File(Image) 가져오기
        List<OverviewImageSummary> overviewImages = fileRepository.findAllByProjectAndType(
                project, FileType.OVERVIEW_IMAGE)
            .stream()
            .map(OverviewImageSummary::from)
            .toList();

        // 프로젝트 id를 가진 프로젝트에 사용되는 Skill 가져오기
        List<ProjectSkillSummary> techStacks = projectSkillRepository.findAllByProject(project)
            .stream()
            .map(ProjectSkillSummary::from)
            .toList();

        // 프로젝트 id를 가진 프로젝트 Member 가져오기
        List<MemberSummary> members = memberRepository.findAllByProject(project)
            .stream()
            .map(this::createMemberSummary)
            .toList();

        return ProjectResponse.from(project, overviewImages, techStacks, members);
    }

    private MemberSummary createMemberSummary(Member member) {
        User user = member.getUser();
        UserSummaryResponse userSummaryResponse = (user == null) ?
            UserSummaryResponse.from(member.getNickname()) :
            UserSummaryResponse.from(user);
        return MemberSummary.from(member, userSummaryResponse);
    }
}
