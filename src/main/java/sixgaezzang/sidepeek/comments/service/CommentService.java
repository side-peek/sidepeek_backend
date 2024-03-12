package sixgaezzang.sidepeek.comments.service;

import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.COMMENT_NOT_EXISTING;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.PARENT_COMMENT_NOT_EXISTING;
import static sixgaezzang.sidepeek.comments.util.validation.CommentValidator.validateCommentId;
import static sixgaezzang.sidepeek.comments.util.validation.CommentValidator.validateParentCommentHasParent;
import static sixgaezzang.sidepeek.comments.util.validation.CommentValidator.validateSaveCommentRequest;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginId;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginIdEqualsOwnerId;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_NOT_EXISTING;

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
import sixgaezzang.sidepeek.users.service.UserService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;

    @Transactional
    public Long save(Long loginId, SaveCommentRequest request) {
        validateLoginId(loginId);
        validateLoginIdEqualsOwnerId(loginId, request.ownerId());
        validateSaveCommentRequest(request);

        User owner = userService.getById(request.ownerId());

        Project project;
        Comment parent = null;
        if (Objects.isNull(request.parentId())) {
            project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXISTING));
        } else {
            parent = getById(request.parentId(), PARENT_COMMENT_NOT_EXISTING);
            validateParentCommentHasParent(parent);

            project = parent.getProject();
        }

        Comment comment = request.toEntity(project, parent, owner);
        commentRepository.save(comment);

        return comment.getProject().getId();
    }

    public Comment getById(Long id, String message) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(message));
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

        Comment comment = getById(commentId, COMMENT_NOT_EXISTING);

        validateLoginIdEqualsOwnerId(loginId, comment.getOwnerId());
        comment.update(request);
    }

    @Transactional
    public void delete(Long loginId, Long commentId) {
        validateLoginId(loginId);
        validateCommentId(commentId);

        Comment comment = getById(commentId, COMMENT_NOT_EXISTING);

        validateLoginIdEqualsOwnerId(loginId, comment.getOwnerId());
        commentRepository.delete(comment);
    }

    private boolean isSameOwner(Comment comment, Project project) {
        return comment.getOwnerId().equals(project.getOwnerId());
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
