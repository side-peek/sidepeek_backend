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
import sixgaezzang.sidepeek.projects.repository.project.ProjectRepository;
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

        project.increaseCommentCount();    // 댓글 수 증가

        return comment.getProject().getId();
    }

    public Comment getById(Long id, String message) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(message));
    }

    public List<CommentResponse> findAll(Long loginId, Project project) {
        List<Comment> comments = commentRepository.findAll(project);

        return comments.stream()
            .map(comment -> {
                boolean isOwner = isSameOwner(comment, loginId);
                List<ReplyResponse> replies = mapReplies(comment, loginId);
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

        decreaseAllCommentCount(comment);    // 댓글 수 감소

        commentRepository.delete(comment);
    }

    private boolean isSameOwner(Comment comment, Long userId) {
        return comment.getOwnerId().equals(userId);
    }

    private List<ReplyResponse> mapReplies(Comment comment, Long userId) {
        List<Comment> replies = commentRepository.findAllReplies(comment);

        return replies.stream()
            .map(reply -> {
                boolean isOwner = isSameOwner(reply, userId);
                return ReplyResponse.from(reply, isOwner);
            })
            .toList();
    }

    private void decreaseAllCommentCount(Comment comment) { // 댓글 삭제 시 대댓글도 삭제하기 위한 메서드
        Project project = comment.getProject();

        long replyCount = commentRepository.countRepliesByParent(comment);  // 대댓글 수 가져오기
        project.decreaseCommentCount(replyCount + 1); // 대댓글 수 + 댓글 수(1) 감소
    }

}
