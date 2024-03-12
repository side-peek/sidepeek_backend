package sixgaezzang.sidepeek.projects.repository;

import static sixgaezzang.sidepeek.like.domain.QLike.like;
import static sixgaezzang.sidepeek.projects.domain.QProject.project;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.response.ProjectBannerResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;

@Repository
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProjectRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ProjectListResponse> findAllBySortAndStatus(List<Long> likedProjectIds,
        String sort,
        boolean isReleased) {
        BooleanExpression deployCondition = isReleased ? project.deployUrl.isNotNull() : null;
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort);

        return queryFactory
            .selectFrom(project)
            .where(deployCondition)
            .orderBy(orderSpecifier, project.id.asc())
            .fetch()
            .stream()
            .map(project -> {
                boolean isLiked = likedProjectIds.contains(project.getId());
                return ProjectListResponse.from(project, isLiked);
            })
            .toList();
    }

    @Override
    public List<ProjectBannerResponse> findAllPopularOfPeriod(LocalDate startDate, LocalDate endDate) {
        List<Project> projects = queryFactory
            .select(project)
            .from(like)
            .join(like.project, project)
            .where(like.createdAt.between(startDate.atStartOfDay(), endDate.atStartOfDay()))
            .distinct()
            .orderBy(project.likeCount.desc())
            .limit(5L)
            .fetch();

        return projects.stream()
            .map(ProjectBannerResponse::from)
            .toList();
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort) {
        switch (sort) {
            case "like":
                return project.likeCount.desc();
            case "view":
                return project.viewCount.desc();
            default:
                return project.createdAt.desc();
        }
    }

}
