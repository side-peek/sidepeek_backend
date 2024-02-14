package sixgaezzang.sidepeek.projects.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.projects.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByNicknameContaining(String keyword);

}
