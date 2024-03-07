package sixgaezzang.sidepeek.like.repository;

import static sixgaezzang.sidepeek.like.domain.QLike.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

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
}
