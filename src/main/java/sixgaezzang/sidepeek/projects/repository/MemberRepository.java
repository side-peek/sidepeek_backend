package sixgaezzang.sidepeek.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    boolean existsByProject(Project project);

    void deleteAllByProject(Project project);

}
