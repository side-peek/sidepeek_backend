package sixgaezzang.sidepeek.auth.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuthProvider is a Querydsl query type for AuthProvider
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthProvider extends EntityPathBase<AuthProvider> {

    private static final long serialVersionUID = 1866775964L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuthProvider authProvider = new QAuthProvider("authProvider");

    public final sixgaezzang.sidepeek.common.domain.QBaseTimeEntity _super = new sixgaezzang.sidepeek.common.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath providerId = createString("providerId");

    public final EnumPath<ProviderType> providerType = createEnum("providerType", ProviderType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final sixgaezzang.sidepeek.users.domain.QUser user;

    public QAuthProvider(String variable) {
        this(AuthProvider.class, forVariable(variable), INITS);
    }

    public QAuthProvider(Path<? extends AuthProvider> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuthProvider(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuthProvider(PathMetadata metadata, PathInits inits) {
        this(AuthProvider.class, metadata, inits);
    }

    public QAuthProvider(Class<? extends AuthProvider> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new sixgaezzang.sidepeek.users.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

