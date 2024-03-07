package sixgaezzang.sidepeek.users.repository.userskill;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.domain.UserSkill;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findAllByUser(User user);

    boolean existsByUser(User user);

    void deleteAllByUser(User user);

}
