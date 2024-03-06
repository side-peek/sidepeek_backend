package sixgaezzang.sidepeek.projects.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = -686970428L;

    public static final QProject project = new QProject("project");

    public final sixgaezzang.sidepeek.common.domain.QBaseTimeEntity _super = new sixgaezzang.sidepeek.common.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath deployUrl = createString("deployUrl");

    public final StringPath description = createString("description");

    public final ComparablePath<java.time.YearMonth> endDate = createComparable("endDate", java.time.YearMonth.class);

    public final StringPath githubUrl = createString("githubUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final StringPath name = createString("name");

    public final StringPath overview = createString("overview");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final ComparablePath<java.time.YearMonth> startDate = createComparable("startDate", java.time.YearMonth.class);

    public final StringPath subName = createString("subName");

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final StringPath troubleshooting = createString("troubleshooting");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QProject(String variable) {
        super(Project.class, forVariable(variable));
    }

    public QProject(Path<? extends Project> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProject(PathMetadata metadata) {
        super(Project.class, metadata);
    }

}

