package sixgaezzang.sidepeek.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.like.domain.Like;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.users.domain.User;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {

    boolean existsByUserAndProject(User user, Project project);

}
