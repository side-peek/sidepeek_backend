package sixgaezzang.sidepeek.skill.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSkill is a Querydsl query type for Skill
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSkill extends EntityPathBase<Skill> {

    private static final long serialVersionUID = 476921443L;

    public static final QSkill skill = new QSkill("skill");

    public final StringPath iconImageUrl = createString("iconImageUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QSkill(String variable) {
        super(Skill.class, forVariable(variable));
    }

    public QSkill(Path<? extends Skill> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSkill(PathMetadata metadata) {
        super(Skill.class, metadata);
    }

}

