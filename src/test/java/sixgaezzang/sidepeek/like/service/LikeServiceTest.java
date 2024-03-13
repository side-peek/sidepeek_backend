package sixgaezzang.sidepeek.like.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.LOGIN_IS_REQUIRED;
import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.LIKE_IS_DUPLICATED;
import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.LIKE_NOT_EXISTING;
import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.PROJECT_ID_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_NOT_EXISTING;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createId;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;
import sixgaezzang.sidepeek.like.domain.Like;
import sixgaezzang.sidepeek.like.dto.request.LikeRequest;
import sixgaezzang.sidepeek.like.dto.response.LikeResponse;
import sixgaezzang.sidepeek.like.repository.LikeRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.repository.project.ProjectRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;
import sixgaezzang.sidepeek.util.FakeDtoProvider;
import sixgaezzang.sidepeek.util.FakeEntityProvider;

@SpringBootTest
@Transactional
@DisplayNameGeneration(ReplaceUnderscores.class)
class LikeServiceTest {

    @Autowired
    LikeService likeService;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    User user;
    Project project;
    Like like;

    private User createAndSaveUser() {
        User newUser = FakeEntityProvider.createUser();
        return userRepository.save(newUser);
    }

    private Project createAndSaveProject(User user) {
        Project newProject = FakeEntityProvider.createProject(user);
        return projectRepository.save(newProject);
    }

    private Like createAndSaveLike(User user, Project project) {
        Like newLike = FakeEntityProvider.createLike(user, project);
        return likeRepository.save(newLike);
    }

    @BeforeEach
    void setUp() {
        user = createAndSaveUser();
        project = createAndSaveProject(user);
    }

    @Nested
    class 좋아요_생성_테스트 {

        @Test
        void 좋아요한_이력이_없으면_좋아요_생성에_성공한다() {
            // given
            LikeRequest request = FakeDtoProvider.createLikeRequest(project.getId());

            // when
            LikeResponse response = likeService.save(user.getId(), request);

            // then
            Optional<Like> like = likeRepository.findById(response.id());
            assertThat(like).isPresent();
            assertThat(like.get()).extracting("project", "user")
                .containsExactly(project, user);
        }

        @Test
        void 로그인하지_않은_사용자일_경우_좋아요_생성에_실패한다() {
            // given
            LikeRequest request = FakeDtoProvider.createLikeRequest(project.getId());

            // when
            ThrowingCallable save = () -> likeService.save(null, request);

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(save)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 사용자가_존재하지_않는_경우_좋아요_생성에_실패한다() {
            // given
            Long invalidUserId = createId();
            LikeRequest request = FakeDtoProvider.createLikeRequest(project.getId());

            // when
            ThrowingCallable save = () -> likeService.save(invalidUserId, request);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(save)
                .withMessage(USER_NOT_EXISTING);
        }

        @Test
        void 좋아요할_프로젝트_Id가_누락되어_좋아요_생성에_실패한다() {
            // given
            LikeRequest request = FakeDtoProvider.createLikeRequest(null);

            // when
            ThrowingCallable save = () -> likeService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(save)
                .withMessage(PROJECT_ID_IS_NULL);
        }

        @Test
        void 좋아요할_프로젝트_Id가_유효하지_않아_좋아요_생성에_실패한다() {
            // given
            Long invalidProjectId = createId();
            LikeRequest request = FakeDtoProvider.createLikeRequest(invalidProjectId);

            // when
            ThrowingCallable save = () -> likeService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(save)
                .withMessage(PROJECT_NOT_EXISTING);
        }

        @Test
        void 이미_좋아요를_눌렀으면_좋아요_생성에_실패한다() {
            // given
            createAndSaveLike(user, project);
            LikeRequest request = FakeDtoProvider.createLikeRequest(project.getId());

            // when
            ThrowingCallable save = () -> likeService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(EntityExistsException.class).isThrownBy(save)
                .withMessage(LIKE_IS_DUPLICATED);
        }
    }

    @Nested
    class 좋아요_삭제_테스트 {

        @Test
        void 좋아요한_이력이_있으면_좋아요_삭제에_성공한다() {
            // given
            Like existingLike = createAndSaveLike(user, project);

            // when
            likeService.delete(user.getId(), existingLike.getId());

            // then
            Optional<Like> like = likeRepository.findById(existingLike.getId());
            assertThat(like).isNotPresent();
        }

        @Test
        void 로그인하지_않은_사용자일_경우_좋아요_삭제에_실패한다() {
            // given
            Like existingLike = createAndSaveLike(user, project);

            // when
            ThrowingCallable delete = () -> likeService.delete(null, existingLike.getId());

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(delete)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 삭제할_좋아요_Id가_유효하지_않아_좋아요_삭제에_실패한다() {
            // given
            Long invalidLikeId = createId();

            // when
            ThrowingCallable delete = () -> likeService.delete(user.getId(), invalidLikeId);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(delete)
                .withMessage(LIKE_NOT_EXISTING);
        }
    }

}
