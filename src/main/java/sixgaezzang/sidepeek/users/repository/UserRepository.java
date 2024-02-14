package sixgaezzang.sidepeek.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.users.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
