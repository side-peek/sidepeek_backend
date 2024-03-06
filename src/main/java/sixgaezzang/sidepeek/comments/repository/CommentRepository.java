package sixgaezzang.sidepeek.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.comments.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

}
