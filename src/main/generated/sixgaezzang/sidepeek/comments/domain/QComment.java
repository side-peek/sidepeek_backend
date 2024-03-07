package sixgaezzang.sidepeek.comments.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QComment is a Querydsl query type for Comment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComment extends EntityPathBase<Comment> {

    private static final long serialVersionUID = -637164464L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComment comment = new QComment("comment");

    public final sixgaezzang.sidepeek.common.domain.QBaseTimeEntity _super = new sixgaezzang.sidepeek.common.domain.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAnonymous = createBoolean("isAnonymous");

    public final QComment parent;

    public final sixgaezzang.sidepeek.projects.domain.QProject project;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final sixgaezzang.sidepeek.users.domain.QUser user;

    public QComment(String variable) {
        this(Comment.class, forVariable(variable), INITS);
    }

    public QComment(Path<? extends Comment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComment(PathMetadata metadata, PathInits inits) {
        this(Comment.class, metadata, inits);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QComment(forProperty("parent"), inits.get("parent")) : null;
        this.project = inits.isInitialized("project") ? new sixgaezzang.sidepeek.projects.domain.QProject(forProperty("project")) : null;
        this.user = inits.isInitialized("user") ? new sixgaezzang.sidepeek.users.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

