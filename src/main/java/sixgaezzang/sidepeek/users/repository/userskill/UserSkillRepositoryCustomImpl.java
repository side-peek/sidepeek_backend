package sixgaezzang.sidepeek.users.repository.userskill;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserSkillRepositoryCustomImpl implements UserSkillRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserSkillRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

}
