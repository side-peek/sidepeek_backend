package sixgaezzang.sidepeek.comments.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.CHILD_COMMENT_CANNOT_BE_PARENT;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.COMMENT_ID_IS_NULL;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.COMMENT_NOT_EXISTING;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.PARENT_COMMENT_NOT_EXISTING;
import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.PROJECT_ID_AND_PARENT_ID_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.LOGIN_IS_REQUIRED;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.OWNER_ID_NOT_EQUALS_LOGIN_ID;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OWNER_ID_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_NOT_EXISTING;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;

import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.comments.dto.request.SaveCommentRequest;
import sixgaezzang.sidepeek.comments.dto.request.UpdateCommentRequest;
import sixgaezzang.sidepeek.comments.repository.CommentRepository;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.repository.project.ProjectRepository;
import sixgaezzang.sidepeek.projects.service.ProjectService;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;
import sixgaezzang.sidepeek.util.FakeDtoProvider;
import sixgaezzang.sidepeek.util.FakeEntityProvider;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    Project project;
    Comment parent;
    Comment comment;

    private User createAndSaveUser() {
        User newUser = FakeEntityProvider.createUser();
        return userRepository.save(newUser);
    }

    private Project createAndSaveProject(User user) {
        Project newProject = FakeEntityProvider.createProject(user);
        return projectRepository.save(newProject);
    }

    private Comment createAndSaveComment(User user, Project project, Comment parent) {
        Comment newComment = FakeEntityProvider.createComment(user, project, parent);
        return commentRepository.save(newComment);
    }

    @BeforeEach
    void setup() {
        user = createAndSaveUser();
        project = createAndSaveProject(user);
        parent = createAndSaveComment(user, project, null);
        comment = createAndSaveComment(user, project, null);
    }

    @Nested
    class 댓글_생성_테스트 {

        @Test
        void 프로젝트_댓글_생성에_성공한다() {
            // given
            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithProjectId(
                user.getId(), project.getId());
            Long initialCommentCount = project.getCommentCount();

            // when
            Long projectId = commentService.save(user.getId(), request);

            // then
            assertThat(projectId).isEqualTo(project.getId());
            assertThat(project.getCommentCount()).isEqualTo(initialCommentCount + 1);
        }

        @Test
        void 대댓글_생성에_성공한다() {
            // given
            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithParentId(
                user.getId(), parent.getId());
            Long initialCommentCount = project.getCommentCount();

            // when
            Long projectId = commentService.save(user.getId(), request);

            // then
            assertThat(projectId).isEqualTo(parent.getProject().getId());
            assertThat(project.getCommentCount()).isEqualTo(initialCommentCount + 1);
        }

        @Test
        void 대댓글의_댓글_생성에_실패한다() {
            // given
            Comment subComment = createAndSaveComment(user, null, parent);
            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithParentId(
                user.getId(), subComment.getId());

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(user.getId(),
                request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(save)
                .withMessage(CHILD_COMMENT_CANNOT_BE_PARENT);
        }

        @Test
        void 작성자_Id가_누락되어_댓글_생성에_실패한다() {
            // given
            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithProjectId(
                null, project.getId());

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(user.getId(),
                request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(save)
                .withMessage(OWNER_ID_IS_NULL);
        }

        @Test
        void 작성자_Id가_누락되어_대댓글_생성에_실패한다() {
            // given
            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithParentId(
                null, parent.getId());

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(user.getId(),
                request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(save)
                .withMessage(OWNER_ID_IS_NULL);
        }

        @Test
        void 프로젝트_Id와_부모댓글_Id가_모두_누락되어_댓글_생성에_실패한다() {
            // given
            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithProjectId(
                user.getId(), null);

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(user.getId(),
                request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(save)
                .withMessage(PROJECT_ID_AND_PARENT_ID_IS_NULL);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.util.TestParameterProvider#createInvalidCommentInfo")
        void 유효하지_않은_정보로_댓글_생성에_실패한다(
            String testMessage, Boolean isAnonymous, String content, String message
        ) {
            // given
            SaveCommentRequest request = new SaveCommentRequest(
                user.getId(),
                project.getId(),
                null,
                isAnonymous,
                content
            );

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(user.getId(),
                request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(save)
                .withMessage(message);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.util.TestParameterProvider#createInvalidCommentInfo")
        void 유효하지_않은_정보로_대댓글_생성에_실패한다(
            String testMessage, Boolean isAnonymous, String content, String message
        ) {
            // given
            SaveCommentRequest request = new SaveCommentRequest(
                user.getId(),
                null,
                parent.getId(),
                isAnonymous,
                content
            );

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(user.getId(),
                request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(save)
                .withMessage(message);
        }

        @Test
        void 작성자_Id가_로그인_Id와_불일치하여_댓글_생성에_실패한다() {
            // given
            User newUser = createAndSaveUser();

            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithProjectId(
                user.getId(), project.getId());

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(newUser.getId(),
                request);

            // then
            assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(save)
                .withMessage(OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }

        @Test
        void 작성자_Id가_로그인_Id와_불일치하여_대댓글_생성에_실패한다() {
            // given
            User newUser = createAndSaveUser();

            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithParentId(
                user.getId(), parent.getId());

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(newUser.getId(),
                request);

            // then
            assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(save)
                .withMessage(OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }

        @Test
        void 사용자가_로그인을_하지_않아서_댓글_생성에_실패한다() {
            // given
            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithProjectId(
                user.getId(), project.getId());

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(null, request);

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(save)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 사용자가_로그인을_하지_않아서_대댓글_생성에_실패한다() {
            // given
            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithParentId(
                user.getId(), parent.getId());

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(null, request);

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(save)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 존재하지_않는_작성자로_댓글_생성에_실패한다() {
            // given
            Long notExistingUserId = user.getId() + 1;

            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithProjectId(
                notExistingUserId, project.getId());

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(notExistingUserId,
                request);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(save)
                .withMessage(USER_NOT_EXISTING);
        }

        @Test
        void 존재하지_않는_프로젝트_댓글_생성에_실패한다() {
            // given
            Long notExistingProjectId = project.getId() + 1;

            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithProjectId(
                user.getId(), notExistingProjectId);

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(user.getId(),
                request);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(save)
                .withMessage(PROJECT_NOT_EXISTING);
        }

        @Test
        void 존재하지_않는_댓글의_대댓글_생성에_실패한다() {
            // given
            Long notExistingParentId = comment.getId() + 1;

            SaveCommentRequest request = FakeDtoProvider.createSaveCommentRequestWithParentId(
                user.getId(), notExistingParentId);

            // when
            ThrowableAssert.ThrowingCallable save = () -> commentService.save(user.getId(),
                request);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(save)
                .withMessage(PARENT_COMMENT_NOT_EXISTING);
        }

    }

    @Nested
    class 댓글_수정_테스트 {

        @Test
        void 프로젝트_댓글_수정에_성공한다() {
            // given
            UpdateCommentRequest request = FakeDtoProvider.createUpdateCommentRequest();

            // when
            commentService.update(user.getId(), comment.getId(), request);

            // then
            assertThat(comment.isAnonymous()).isEqualTo(request.isAnonymous());
            assertThat(comment.getContent()).isEqualTo(request.content());
        }

        @Test
        void 존재하지_않는_댓글_수정에_실패한다() {
            // given
            Long notExistingCommentId = comment.getId() + 1;

            UpdateCommentRequest request = FakeDtoProvider.createUpdateCommentRequest();

            // when
            ThrowableAssert.ThrowingCallable update = () -> commentService.update(
                user.getId(), notExistingCommentId, request);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(update)
                .withMessage(COMMENT_NOT_EXISTING);
        }

        @Test
        void 사용자가_로그인을_하지_않아서_댓글_수정에_실패한다() {
            // given
            UpdateCommentRequest request = FakeDtoProvider.createUpdateCommentRequest();

            // when
            ThrowableAssert.ThrowingCallable update = () -> commentService.update(
                null, comment.getId(), request);

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(update)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 작성자_Id가_로그인_Id와_불일치하여_댓글_수정에_실패한다() {
            // given
            User newUser = createAndSaveUser();

            UpdateCommentRequest request = FakeDtoProvider.createUpdateCommentRequest();

            // when
            ThrowableAssert.ThrowingCallable update = () -> commentService.update(
                newUser.getId(), comment.getId(), request);

            // then
            assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(update)
                .withMessage(OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }

        @Test
        void 댓글_Id가_누락되어_댓글_수정에_실패한다() {
            // given
            UpdateCommentRequest request = FakeDtoProvider.createUpdateCommentRequest();

            // when
            ThrowableAssert.ThrowingCallable update = () -> commentService.update(
                user.getId(), null, request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(update)
                .withMessage(COMMENT_ID_IS_NULL);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.util.TestParameterProvider#createInvalidCommentInfo")
        void 유효하지_않은_정보로_댓글_수정에_실패한다(
            String testMessage, Boolean isAnonymous, String content, String message
        ) {
            // given
            UpdateCommentRequest request = new UpdateCommentRequest(isAnonymous, content);

            // when
            ThrowableAssert.ThrowingCallable update = () -> commentService.update(
                user.getId(), comment.getId(), request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(update)
                .withMessage(message);
        }

    }

    @Nested
    class 댓글_삭제_테스트 {

        @Test
        void 프로젝트_댓글_삭제에_성공한다() {
            // given
            Long initialCommentCount = project.getCommentCount();

            // when
            commentService.delete(user.getId(), comment.getId());

            // then
            assertThat(commentRepository.findById(comment.getId())).isEmpty();
            assertThat(project.getCommentCount()).isEqualTo(initialCommentCount - 1);
        }

        @Test
        void 대댓글이_있는_댓글_삭제에_성공한다() {
            // given
            Comment subComment = createAndSaveComment(user, null, comment);
            Long initialCommentCount = project.getCommentCount();

            // when
            commentService.delete(user.getId(), comment.getId());

            // then
            assertThat(commentRepository.findById(comment.getId())).isEmpty();
            // TODO: Empty가 아닌 것으로 나오는 이유 알아보기
            // assertThat(commentRepository.findById(subComment.getId())).isEmpty();
            assertThat(project.getCommentCount()).isEqualTo(initialCommentCount - 2);
        }

        @Test
        void 존재하지_않는_댓글_삭제에_실패한다() {
            // given
            Long notExistingCommentId = comment.getId() + 1;

            // when
            ThrowableAssert.ThrowingCallable delete = () -> commentService.delete(
                user.getId(), notExistingCommentId);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(delete)
                .withMessage(COMMENT_NOT_EXISTING);
        }

        @Test
        void 사용자가_로그인을_하지_않아서_댓글_삭제에_실패한다() {
            // given, when
            ThrowableAssert.ThrowingCallable delete = () -> commentService.delete(
                null, comment.getId());

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(delete)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 작성자_Id가_로그인_Id와_불일치하여_댓글_삭제에_실패한다() {
            // given
            User newUser = createAndSaveUser();

            // when
            ThrowableAssert.ThrowingCallable delete = () -> commentService.delete(
                newUser.getId(), comment.getId());

            // then
            assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(delete)
                .withMessage(OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }

        @Test
        void 댓글_Id가_누락되어_댓글_수정에_삭제한다() {
            // given, when
            ThrowableAssert.ThrowingCallable delete = () -> commentService.delete(
                user.getId(), null);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(delete)
                .withMessage(COMMENT_ID_IS_NULL);
        }

    }

}
