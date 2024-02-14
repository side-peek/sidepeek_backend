package sixgaezzang.sidepeek.skill.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.skill.domain.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findAllByNameContaining(String searchKeyword);
}
