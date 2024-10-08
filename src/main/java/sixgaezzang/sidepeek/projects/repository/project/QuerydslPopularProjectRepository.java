package sixgaezzang.sidepeek.projects.repository.project;

import static sixgaezzang.sidepeek.like.domain.QLike.like;
import static sixgaezzang.sidepeek.projects.domain.QProject.project;

import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSummary;

@Repository
public class QuerydslPopularProjectRepository implements PopularProjectRepository {

    private final JPAQueryFactory queryFactory;

    public QuerydslPopularProjectRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 특정 기간 동안 인기 프로젝트를 조회합니다.
     * - 기준
     *  1. 해당 기간 동안 받은 좋아요 수
     *  2. 전체 좋아요 수
     *  3. 최신 순
     */
    @Override
    public List<ProjectSummary> findRankBetweenPeriod(
        LocalDate startDate,
        LocalDate endDate,
        int count
    ) {
        DateTemplate<LocalDate> createdAt = Expressions.dateTemplate(
            LocalDate.class, "DATE_FORMAT({0}, {1})", like.createdAt, "%Y-%m-%d");

        List<Project> projects = queryFactory
            .select(project)
            .from(like)
            .join(like.project, project)
            .where(createdAt.between(startDate, endDate))
            .groupBy(project)
            .orderBy(like.count().desc())
            .limit(count)
            .fetch();

        return projects.stream()
            .map(ProjectSummary::from)
            .toList();
    }
}
