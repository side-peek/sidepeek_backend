package sixgaezzang.sidepeek.comments.repository;

import java.util.List;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.projects.domain.Project;

public interface CommentRepositoryCustom {

    List<Comment> findAll(Project project);

    List<Comment> findAllReplies(Comment parent);

}
