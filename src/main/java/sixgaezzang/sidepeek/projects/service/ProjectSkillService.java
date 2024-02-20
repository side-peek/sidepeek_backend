package sixgaezzang.sidepeek.projects.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectSkillService {

    ProjectSkillRepository projectSkillRepository;

    @Transactional
    public void saveAll(Long projectId, List<ProjectSkillSaveRequest> projectSkillSaveRequests) {
        List<ProjectSkill> skills = projectSkillSaveRequests.stream().map(
            skill -> ProjectSkill.builder()
                .projectId(projectId)
                .skillId(skill.skillId())
                .category(skill.category())
                .build()
        ).toList();

        projectSkillRepository.saveAll(skills);
    }
}
