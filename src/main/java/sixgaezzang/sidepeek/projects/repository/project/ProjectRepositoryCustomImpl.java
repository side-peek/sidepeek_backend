package sixgaezzang.sidepeek.projects.repository.project;

import static sixgaezzang.sidepeek.comments.domain.QComment.comment;
import static sixgaezzang.sidepeek.like.domain.QLike.like;
import static sixgaezzang.sidepeek.projects.domain.QProject.project;
import static sixgaezzang.sidepeek.projects.domain.QProjectSkill.projectSkill;
import static sixgaezzang.sidepeek.projects.domain.member.QMember.member;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.QProject;
import sixgaezzang.sidepeek.projects.dto.request.FindProjectRequest;
import sixgaezzang.sidepeek.projects.dto.request.SortType;
import sixgaezzang.sidepeek.projects.dto.response.CursorPaginationResponse;
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
        FindProjectRequest request) {
        // where
        BooleanExpression deployCondition =
            request.isReleased() ? project.deployUrl.isNotNull() : null;
        BooleanExpression skillCondition = getSkillCondition(request.skill());

        BooleanExpression searchCondition = getSearchCondition(request.search());

        BooleanExpression cursorCondition = getCursorCondition(request.sort(),
            request.lastProjectId(), request.lastOrderCount());

        // orderBy
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(request.sort());

        Long totalElements = 0L;

        JPAQuery<Project> query = queryFactory
            .selectFrom(project);

        if (searchCondition != null) {
            query
                .join(member).on(project.id.eq(member.project.id))
                .where(searchCondition);
            totalElements = getTotalElements(member,
                Arrays.asList(deployCondition, skillCondition, searchCondition),
                member.project);
        } else {
            totalElements = getTotalElements(project,
                Arrays.asList(deployCondition, skillCondition), project);
        }

        List<ProjectListResponse> results = query
            .where(
                deployCondition,
                skillCondition,
                cursorCondition
            )
            .orderBy(orderSpecifier, project.id.desc())
            .limit(request.pageSize() + 1)
            .fetch()
            .stream()
            .map(project -> {
                boolean isLiked = likedProjectIds.contains(project.getId());
                return ProjectListResponse.from(project, isLiked);
            })
            .toList();

        return checkEndPage(results, request.pageSize(), totalElements);
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
            .select(project.countDistinct())
            .from(from)
            .join(join, project)
            .where(condition)
            .fetchOne();
    }

    private Long getTotalElements(EntityPathBase<?> from,
        List<BooleanExpression> conditions,
        QProject join) {
        BooleanExpression combinedCondition = conditions.stream()
            .filter(Objects::nonNull)
            .reduce(BooleanExpression::and)
            .orElse(null);

        JPAQuery<Long> query = queryFactory
            .select(project.countDistinct())
            .from(from);

        // 조인 대상 엔티티와 기준 엔티티가 다를 경우에만 조인을 수행하도록 합니다.
        if (!from.equals(join)) {
            query.join(join, project);
        }

        return query
            .where(combinedCondition)
            .fetchOne();
    }

    private List<ProjectListResponse> toProjectListResponseList(List<Long> likedProjectIds,
        List<Project> projects) {
        return projects.stream()
            .map(project -> ProjectListResponse.from(project,
                likedProjectIds.contains(project.getId())))
            .toList();
    }

    private BooleanExpression getCursorCondition(SortType sort, Long lastProjectId,
        Long lastOrderCount) {
        if (lastProjectId == null && lastOrderCount == null) {  // 첫 번째 페이지
            return null;
        }

        switch (sort) { // 다음 페이지
            case like: // 좋아요순
                return project.likeCount.eq(lastOrderCount)
                    .and(project.id.gt(lastProjectId))
                    .or(project.likeCount.lt(lastOrderCount));
            case view: // 조회수순
                return project.viewCount.eq(lastOrderCount)
                    .and(project.id.gt(lastProjectId))
                    .or(project.viewCount.lt(lastOrderCount));
            default: // 최신순
                return project.id.lt(lastProjectId);
        }
    }

    private BooleanExpression getSearchCondition(String search) {
        if (Objects.isNull(search) || search.isEmpty()) {
            return null;
        }

        String keyword = "%" + search.trim() + "%";
        return project.name.likeIgnoreCase(keyword)
            .or(member.nickname.likeIgnoreCase(keyword));
    }

    public BooleanExpression getSkillCondition(List<String> skillNames) {
        if (Objects.isNull(skillNames) || skillNames.isEmpty()) {
            return null;
        }

        // 프로젝트 ID 서브쿼리를 생성하여 스킬을 모두 포함하는 프로젝트를 찾기
        JPAQuery<Long> projectHasSkillsSubQuery = queryFactory
            .select(projectSkill.project.id)
            .from(projectSkill)
            .join(projectSkill.skill)
            .where(projectSkill.skill.name.in(skillNames))
            .groupBy(projectSkill.project.id)
            .having(projectSkill.project.id.count().eq(Expressions.constant(skillNames.size())));

        // 프로젝트 ID 서브쿼리와 매칭되는 프로젝트를 찾는 조건을 반환합니다.
        return project.id.in(projectHasSkillsSubQuery);
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
