package sixgaezzang.sidepeek.like.repository;

import static sixgaezzang.sidepeek.like.domain.QLike.like;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.users.domain.User;

@Repository
public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Long> findAllProjectIdsByUser(Long userId) {
        return queryFactory
            .select(like.project.id)
            .from(like)
            .where(like.user.id.eq(userId))
            .fetch();
    }

    @Override
    public Optional<Long> findIdByUserAndProject(User user,
        Project project) {
        Predicate predicate = like.user.eq(user).and(like.project.eq(project));

        return Optional.ofNullable(queryFactory
            .select(like.id)
            .from(like)
            .where(predicate)
            .fetchFirst()
        );
    }
}
