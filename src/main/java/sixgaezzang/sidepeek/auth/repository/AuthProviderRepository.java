package sixgaezzang.sidepeek.auth.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.auth.domain.ProviderType;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {

    @Query("SELECT a FROM AuthProvider a JOIN FETCH a.user WHERE a.providerType = :providerType AND a.providerId = :providerId")
    Optional<AuthProvider> findByProviderTypeAndProviderId(ProviderType providerType,
        String providerId);
}
