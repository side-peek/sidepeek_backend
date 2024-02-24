package sixgaezzang.sidepeek.auth.repository;

import org.springframework.data.repository.CrudRepository;
import sixgaezzang.sidepeek.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

}
