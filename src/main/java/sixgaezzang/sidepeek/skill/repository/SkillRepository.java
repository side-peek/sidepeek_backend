package sixgaezzang.sidepeek.skill.repository;

import java.util.List;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.skill.domain.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findAllByNameContaining(String keyword);


    Slice<Skill> findAllByNameStartingWithOrderByName(String keyword, Pageable pageable);

    List<Skill> findAllByNameStartingWithAndNameGreaterThanOrderByName(String keyword, String lastKeyword,
                                                                       Limit limit);


}
