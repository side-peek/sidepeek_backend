package sixgaezzang.sidepeek.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long>,
    AuthProviderRepositoryCustom {

}
