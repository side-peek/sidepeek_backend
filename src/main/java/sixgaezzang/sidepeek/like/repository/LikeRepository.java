package sixgaezzang.sidepeek.like.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.like.domain.Like;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.users.domain.User;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {

    Optional<Like> findByUserAndProject(User user, Project project);

}
