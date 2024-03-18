package sixgaezzang.sidepeek.like.repository;

import java.util.List;
import java.util.Optional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.users.domain.User;

public interface LikeRepositoryCustom {

    List<Long> findAllProjectIdsByUser(Long userId);

    Optional<Long> findIdByUserAndProject(User user, Project project);

}
