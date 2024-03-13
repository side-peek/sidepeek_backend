package sixgaezzang.sidepeek.comments.service;

import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.COMMENT_NOT_EXISTING;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.PARENT_COMMENT_NOT_EXISTING;
import static sixgaezzang.sidepeek.comments.util.validation.CommentValidator.validateCommentId;
import static sixgaezzang.sidepeek.comments.util.validation.CommentValidator.validateParentCommentHasParent;
import static sixgaezzang.sidepeek.comments.util.validation.CommentValidator.validateSaveCommentRequest;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginId;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginIdEqualsOwnerId;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_NOT_EXISTING;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.comments.dto.request.SaveCommentRequest;
import sixgaezzang.sidepeek.comments.dto.request.UpdateCommentRequest;
import sixgaezzang.sidepeek.comments.dto.response.CommentResponse;
import sixgaezzang.sidepeek.comments.dto.response.ReplyResponse;
import sixgaezzang.sidepeek.comments.repository.CommentRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public Long save(Long loginId, SaveCommentRequest request) {
        validateLoginId(loginId);
        validateLoginIdEqualsOwnerId(loginId, request.ownerId());
        validateSaveCommentRequest(request);

        User owner = userRepository.findById(request.ownerId())
            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXISTING));

        Project project;
        Comment parent = null;
        if (Objects.isNull(request.parentId())) {
            project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXISTING));
        } else {
            parent = commentRepository.findById(request.parentId())
                .orElseThrow(() -> new EntityNotFoundException(PARENT_COMMENT_NOT_EXISTING));
            validateParentCommentHasParent(parent);

            project = parent.getProject();
        }

        Comment comment = request.toEntity(project, parent, owner);
        commentRepository.save(comment);

        return comment.getProject().getId();
    }

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

    @Transactional
    public void update(Long loginId, Long commentId, UpdateCommentRequest request) {
        validateLoginId(loginId);
        validateCommentId(commentId);

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_EXISTING));

        validateLoginIdEqualsOwnerId(loginId, comment.getUser().getId());
        comment.update(request);
    }

    @Transactional
    public void delete(Long loginId, Long commentId) {
        validateLoginId(loginId);
        validateCommentId(commentId);

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_EXISTING));

        validateLoginIdEqualsOwnerId(loginId, comment.getUser().getId());
        commentRepository.delete(comment);
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
