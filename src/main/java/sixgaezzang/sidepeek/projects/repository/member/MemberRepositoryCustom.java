package sixgaezzang.sidepeek.projects.repository.member;

import java.util.List;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;

public interface MemberRepositoryCustom {

    List<MemberSummary> findAllWithUser(Project project);

}
