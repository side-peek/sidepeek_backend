package sixgaezzang.sidepeek.auth.repository;

import static sixgaezzang.sidepeek.auth.domain.QAuthProvider.authProvider;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.auth.domain.ProviderType;

@Repository
public class AuthProviderRepositoryCustomImpl implements AuthProviderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AuthProviderRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    
    @Override
    public Optional<AuthProvider> findByProviderTypeAndProviderId(ProviderType providerType,
        String providerId) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(authProvider)
                .join(authProvider.user).fetchJoin()
                .where(
                    authProvider.providerType.eq(providerType),
                    authProvider.providerId.eq(providerId)
                )
                .fetchOne()
        );
    }
}
