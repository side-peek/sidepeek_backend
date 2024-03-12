package sixgaezzang.sidepeek.comments.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.comments.dto.response.CommentResponse;
import sixgaezzang.sidepeek.comments.dto.response.CommentWithCountResponse;
import sixgaezzang.sidepeek.comments.dto.response.ReplyResponse;
import sixgaezzang.sidepeek.comments.repository.CommentRepository;
import sixgaezzang.sidepeek.projects.domain.Project;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentWithCountResponse findAll(Project project) {
        List<Comment> comments = commentRepository.findAll(project);
        AtomicLong commentCount = new AtomicLong((long) comments.size());    // 댓글 개수

        if (comments.isEmpty()) {
            return CommentWithCountResponse.from(null, commentCount.get()); // 댓글이 없는 경우 null 반환
        }

        List<CommentResponse> results = comments.stream()
            .map(comment -> {
                boolean isOwner = isSameOwner(comment, project);
                List<ReplyResponse> replies = mapReplies(comment);
                if (Objects.nonNull(replies)) {
                    commentCount.addAndGet(replies.size()); // 대댓글 개수 추가
                }
                return CommentResponse.from(comment, isOwner, replies);
            })
            .toList();

        return CommentWithCountResponse.from(results, commentCount.get());
    }

    private boolean isSameOwner(Comment comment, Project project) {
        return comment.getUser().getId().equals(project.getOwnerId());
    }

    private List<ReplyResponse> mapReplies(Comment comment) {
        List<Comment> replies = commentRepository.findAllReplies(comment);

        if (replies.isEmpty()) {
            return null;    // 대댓글이 없는 경우 null 반환
        }

        return replies.stream()
            .map(reply -> {
                boolean isOwner = isSameOwner(reply, reply.getProject());
                return ReplyResponse.from(reply, isOwner);
            })
            .toList();
    }

}
