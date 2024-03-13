package sixgaezzang.sidepeek.like.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.PROJECT_ID_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_NOT_EXISTING;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createId;

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
    class 좋아요_토글_테스트 {

        @Test
        void 좋아요한_이력이_없으면_좋아요를_생성한다() {
            // given
            LikeRequest request = FakeDtoProvider.createLikeRequest(project.getId());

            // when
            LikeResponse response = likeService.toggle(user.getId(), request);

            // then
            Optional<Like> like = likeRepository.findByUserAndProject(user, project);
            assertThat(like).isPresent();
            assertThat(response.isLiked()).isTrue();
        }

        @Test
        void 좋아요한_이력이_있으면_좋아요를_삭제한다() {
            // given
            Like existingLike = createAndSaveLike(user, project);
            LikeRequest request = FakeDtoProvider.createLikeRequest(project.getId());

            // when
            LikeResponse response = likeService.toggle(user.getId(), request);

            // then
            Optional<Like> like = likeRepository.findByUserAndProject(user, project);
            assertThat(like).isNotPresent();
            assertThat(response.isLiked()).isFalse();
        }

        @Test
        void 좋아요할_프로젝트_Id가_누락되어_좋아요_토글에_실패한다() {
            // given
            LikeRequest request = FakeDtoProvider.createLikeRequest(null);

            // when
            ThrowingCallable toggle = () -> likeService.toggle(user.getId(), request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(toggle)
                .withMessage(PROJECT_ID_IS_NULL);
        }

        @Test
        void 좋아요할_프로젝트_Id가_유효하지_않아_좋아요_토글에_실패한다() {
            // given
            Long invalidProjectId = createId();
            LikeRequest request = FakeDtoProvider.createLikeRequest(invalidProjectId);

            // when
            ThrowingCallable toggle = () -> likeService.toggle(user.getId(), request);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(toggle)
                .withMessage(PROJECT_NOT_EXISTING);
        }
    }
}
