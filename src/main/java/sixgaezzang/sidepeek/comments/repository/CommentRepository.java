package sixgaezzang.sidepeek.comments.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.projects.domain.Project;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByProject(Project project);

    List<Comment> findAllByParent(Comment comment);

}
