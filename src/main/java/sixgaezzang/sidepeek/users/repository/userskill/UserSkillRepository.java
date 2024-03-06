package sixgaezzang.sidepeek.users.repository.userskill;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.users.domain.UserSkill;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long>, UserSkillRepositoryCustom {
}
