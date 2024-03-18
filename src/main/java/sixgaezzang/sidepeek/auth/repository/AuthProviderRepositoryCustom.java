package sixgaezzang.sidepeek.auth.repository;

import java.util.Optional;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.auth.domain.ProviderType;

public interface AuthProviderRepositoryCustom {

    Optional<AuthProvider> findByProviderTypeAndProviderId(ProviderType providerType,
        String providerId);
}
