package sixgaezzang.sidepeek.comments.repository;

import static sixgaezzang.sidepeek.comments.domain.QComment.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.projects.domain.Project;

@Repository
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Comment> findAll(Project project) {
        return queryFactory
            .selectFrom(comment)
            .where(comment.project.eq(project)
                .and(comment.parent.id.isNull()))
            .fetch();
    }

    @Override
    public List<Comment> findAllReplies(Comment parent) {
        return queryFactory
            .selectFrom(comment)
            .where(comment.parent.eq(parent))
            .fetch();
    }

    @Override
    public long countRepliesByParent(Comment parent) {
        return queryFactory
            .select(comment.count())
            .from(comment)
            .where(comment.parent.eq(parent))
            .fetchOne();
    }
}
