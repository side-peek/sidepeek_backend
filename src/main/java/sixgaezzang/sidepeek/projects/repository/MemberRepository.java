package sixgaezzang.sidepeek.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.projects.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
