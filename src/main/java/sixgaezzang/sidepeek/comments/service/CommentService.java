package sixgaezzang.sidepeek.comments.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.comments.dto.response.CommentResponse;
import sixgaezzang.sidepeek.comments.dto.response.ReplyResponse;
import sixgaezzang.sidepeek.comments.repository.CommentRepository;
import sixgaezzang.sidepeek.projects.domain.Project;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentResponse> findAll(Project project) {
        List<Comment> comments = commentRepository.findAll(project);
        return comments.stream()
            .map(comment -> {
                boolean isOwner = isSameOwner(comment, project);
                List<ReplyResponse> replies = mapReplies(comment);
                return CommentResponse.from(comment, isOwner, replies);
            })
            .toList();
    }

    private boolean isSameOwner(Comment comment, Project project) {
        return comment.getUser().getId().equals(project.getOwnerId());
    }

    private List<ReplyResponse> mapReplies(Comment comment) {
        List<Comment> replies = commentRepository.findAllReplies(comment);
        return replies.stream()
            .map(reply -> {
                boolean isOwner = isSameOwner(reply, reply.getProject());
                return ReplyResponse.from(reply, isOwner);
            })
            .toList();
    }

}
