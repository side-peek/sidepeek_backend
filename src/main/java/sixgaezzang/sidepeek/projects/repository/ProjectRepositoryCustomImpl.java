package sixgaezzang.sidepeek.projects.repository;

import static sixgaezzang.sidepeek.projects.domain.QProject.project;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
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

        List<Project> projects = queryFactory
            .selectFrom(project)
            .where(deployCondition)
            .orderBy(orderSpecifier, project.id.asc())
            .fetch();

        return toProjectListResponseList(likedProjectIds, projects);
    }

    @Override
    private List<ProjectListResponse> toProjectListResponseList(List<Long> likedProjectIds,
        List<Project> projects) {
        return projects.stream()
            .map(project -> ProjectListResponse.from(project,
                likedProjectIds.contains(project.getId())))
            .toList();
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort) {
        return switch (sort) {
            case "like" -> project.likeCount.desc();
            case "view" -> project.viewCount.desc();
            default -> project.createdAt.desc();
        };
    }
}
