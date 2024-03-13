package sixgaezzang.sidepeek.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.users.domain.User;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long>,
    AuthProviderRepositoryCustom {
    boolean existsByUser(User user);
}
