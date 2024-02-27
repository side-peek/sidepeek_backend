package sixgaezzang.sidepeek.projects.repository;

import static sixgaezzang.sidepeek.projects.domain.member.QMember.member;
import static sixgaezzang.sidepeek.users.domain.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberSummary> findAllWithUser(Project project) {
        return queryFactory
            .select(Projections.constructor(MemberSummary.class,
                member.id, member.role,
                Projections.constructor(UserSummary.class,
                    user.id,
                    new CaseBuilder()
                        .when(user.id.isNull())
                        .then(member.nickname)
                        .otherwise(user.nickname),
                    user.profileImageUrl)))
            .from(member)
            .leftJoin(member.user, user)
            .where(member.project.eq(project))
            .fetch();
    }
}
