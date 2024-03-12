package sixgaezzang.sidepeek.projects.repository.project;

import static sixgaezzang.sidepeek.projects.domain.QProject.project;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import sixgaezzang.sidepeek.projects.dto.request.CursorPaginationInfoRequest;
import sixgaezzang.sidepeek.projects.dto.request.SortType;
import sixgaezzang.sidepeek.projects.dto.response.CursorPaginationResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;

@Slf4j
@Repository
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProjectRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public CursorPaginationResponse<ProjectListResponse> findByCondition(
        List<Long> likedProjectIds,
        CursorPaginationInfoRequest pageable) {
        BooleanExpression deployCondition =
            pageable.isReleased() ? project.deployUrl.isNotNull() : null;
        BooleanExpression cursorCondition = getCursorCondition(pageable.sort(),
            pageable.lastProjectId(), pageable.lastOrderCount());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageable.sort());

        List<ProjectListResponse> results = queryFactory
            .selectFrom(project)
            .where(
                deployCondition,
                cursorCondition
            )
            .orderBy(orderSpecifier, project.id.desc())
            .limit(pageable.pageSize())
            .fetch()
            .stream()
            .map(project -> {
                boolean isLiked = likedProjectIds.contains(project.getId());
                return ProjectListResponse.from(project, isLiked);
            })
            .toList();

        return checkEndPage(results, pageable.pageSize());
    }

    private BooleanExpression getCursorCondition(SortType sort, Long lastProjectId,
        Long orderCount) {
        if (lastProjectId == null && orderCount == null) {
            log.info("첫 번째 페이지");
            return null;
        }

        log.info("두 번째 페이지");
        switch (sort) {
            case like: // 좋아요순
                return project.likeCount.eq(orderCount)
                    .and(project.id.gt(lastProjectId))
                    .or(project.likeCount.lt(orderCount));
            case view: // 조회수순
                return project.viewCount.eq(orderCount)
                    .and(project.id.gt(lastProjectId))
                    .or(project.viewCount.lt(orderCount));
            default: // 최신순
                return project.id.lt(lastProjectId);
        }
    }

    private OrderSpecifier<?> getOrderSpecifier(SortType sort) {
        switch (sort) {
            case like:
                return project.likeCount.desc();
            case view:
                return project.viewCount.desc();
            default:
                return project.createdAt.desc();
        }
    }

    private CursorPaginationResponse<ProjectListResponse> checkEndPage(
        List<ProjectListResponse> results, int pageSize) {
        boolean hasNext = false;

        if (results.size() > pageSize) { //다음 게시물이 있는 경우
            hasNext = true;
            results = results.subList(0, results.size());
        }

        return CursorPaginationResponse.from(results, hasNext);
    }

}
