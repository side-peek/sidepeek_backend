package sixgaezzang.sidepeek.projects.repository.project;

import static sixgaezzang.sidepeek.comments.domain.QComment.comment;
import static sixgaezzang.sidepeek.like.domain.QLike.like;
import static sixgaezzang.sidepeek.projects.domain.QProject.project;
import static sixgaezzang.sidepeek.projects.domain.member.QMember.member;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.QProject;
import sixgaezzang.sidepeek.projects.dto.request.CursorPaginationInfoRequest;
import sixgaezzang.sidepeek.projects.dto.request.SortType;
import sixgaezzang.sidepeek.projects.dto.response.CursorPaginationResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectBannerResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.users.domain.User;

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
        long totalElements = getTotalElementsByCondition(deployCondition);

        List<ProjectListResponse> results = queryFactory
            .selectFrom(project)
            .where(
                deployCondition,
                cursorCondition
            )
            .orderBy(orderSpecifier, project.id.desc())
            .limit(pageable.pageSize() + 1)
            .stream()
            .map(project -> {
                boolean isLiked = likedProjectIds.contains(project.getId());
                return ProjectListResponse.from(project, isLiked);
            })
            .toList();

        return checkEndPage(results, pageable.pageSize(), totalElements);
    }

    @Override
    public Page<ProjectListResponse> findAllByUserJoined(List<Long> likedProjectIds, User user,
                                                         Pageable pageable) {
        BooleanExpression memberCondition = member.user.eq(user);
        return findPageByCondition(member, member.project, memberCondition, pageable,
            likedProjectIds);
    }

    @Override
    public Page<ProjectListResponse> findAllByUserLiked(List<Long> likedProjectIds, User user,
                                                        Pageable pageable) {
        BooleanExpression likeCondition = like.user.eq(user);
        return findPageByCondition(like, like.project, likeCondition, pageable, likedProjectIds);
    }

    @Override
    public Page<ProjectListResponse> findAllByUserCommented(List<Long> likedProjectIds, User user,
                                                            Pageable pageable) {
        BooleanExpression commentCondition = comment.user.eq(user);
        return findPageByCondition(comment, comment.project, commentCondition, pageable,
            likedProjectIds);
    }

    private Page<ProjectListResponse> findPageByCondition(EntityPathBase<?> from,
                                                          QProject join, BooleanExpression condition, Pageable pageable,
                                                          List<Long> likedProjectIds) {
        List<Project> projects = queryFactory
            .select(project)
            .from(from)
            .join(join, project)
            .where(condition)
            .orderBy(project.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<ProjectListResponse> projectDTOs = toProjectListResponseList(likedProjectIds,
            projects);
        long count = getCount(from, condition, join);

        return new PageImpl<>(projectDTOs, pageable, count);
    }

    private Long getCount(EntityPathBase<?> from, BooleanExpression condition, QProject join) {
        return queryFactory
            .select(project.count())
            .from(from)
            .join(join, project)
            .where(condition)
            .fetchOne();
    }

    private List<ProjectListResponse> toProjectListResponseList(List<Long> likedProjectIds,
                                                                List<Project> projects) {
        return projects.stream()
            .map(project -> ProjectListResponse.from(project,
                likedProjectIds.contains(project.getId())))
            .toList();
    }

    @Override
    public List<ProjectBannerResponse> findAllPopularOfPeriod(LocalDate startDate, LocalDate endDate, int count) {
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
            .map(ProjectBannerResponse::from)
            .toList();
    }

    private long getTotalElementsByCondition(BooleanExpression deployCondition) {
        NumberTemplate<Long> countTemplate = Expressions.numberTemplate(Long.class, "COUNT({0})",
            project.id);

        return queryFactory
            .select(countTemplate)
            .from(project)
            .where(deployCondition)
            .fetchOne();
    }

    private BooleanExpression getCursorCondition(SortType sort, Long lastProjectId,
        Long orderCount) {
        if (lastProjectId == null && orderCount == null) {  // 첫 번째 페이지
            return null;
        }

        switch (sort) { // 다음 페이지
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
        List<ProjectListResponse> results, int pageSize, long totalElements) {
        boolean hasNext = false;

        if (results.size() > pageSize) { //다음 게시물이 있는 경우
            hasNext = true;
            results = results.subList(0, pageSize);
        }

        return CursorPaginationResponse.from(results, totalElements, hasNext);
    }

}
