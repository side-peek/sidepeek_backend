package sixgaezzang.sidepeek.projects.repository;

import static sixgaezzang.sidepeek.comments.domain.QComment.comment;
import static sixgaezzang.sidepeek.like.domain.QLike.like;
import static sixgaezzang.sidepeek.projects.domain.QProject.project;
import static sixgaezzang.sidepeek.projects.domain.member.QMember.member;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
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
            .distinct()
            .orderBy(project.likeCount.desc())
            .limit(count)
            .fetch();

        return projects.stream()
            .map(ProjectBannerResponse::from)
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
