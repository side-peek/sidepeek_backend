package sixgaezzang.sidepeek.projects.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByProject(Project project);

    boolean existsByProject(Project project);

    void deleteAllByProject(Project project);

}
