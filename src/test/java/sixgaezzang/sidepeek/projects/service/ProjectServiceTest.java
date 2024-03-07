package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.OWNER_ID_NOT_EQUALS_LOGIN_ID;
import static sixgaezzang.sidepeek.common.util.CommonConstant.LOGIN_IS_REQUIRED;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OWNER_ID_IS_NULL;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_SKILL_COUNT;

import jakarta.persistence.EntityNotFoundException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.comments.dto.response.CommentResponse;
import sixgaezzang.sidepeek.comments.repository.CommentRepository;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.request.MemberSaveRequest;
import sixgaezzang.sidepeek.projects.dto.request.ProjectRequest;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.exception.ProjectErrorCode;
import sixgaezzang.sidepeek.projects.repository.project.ProjectRepository;
import sixgaezzang.sidepeek.projects.util.FakeDtoProvider;
import sixgaezzang.sidepeek.projects.util.FakeEntityProvider;
import sixgaezzang.sidepeek.projects.util.FakeValueProvider;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(ReplaceUnderscores.class)
class ProjectServiceTest {

    static final Faker faker = new Faker();
    static final int MEMBER_COUNT = MAX_MEMBER_COUNT / 2;
    static final int PROJECT_SKILL_COUNT = MAX_PROJECT_SKILL_COUNT / 2;
    static List<MemberSaveRequest> members;
    static List<Long> fellowMemberIds;
    static List<ProjectSkillSaveRequest> techStacks;
    static String NAME = FakeValueProvider.createProjectName();
    static String OVERVIEW = FakeValueProvider.createOverview();
    static String GITHUB_URL = FakeValueProvider.createUrl();
    static String DESCRIPTION = FakeValueProvider.createLongText();

    @Autowired
    ProjectService projectService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    CommentRepository commentRepository;

    User user;

    private Skill createAndSaveSkill() {
        return skillRepository.save(FakeEntityProvider.createSkill());
    }

    private User createAndSaveUser() {
        User newUser = FakeEntityProvider.createUser();
        return userRepository.save(newUser);
    }

    private Project createAndSaveProject(User user) {
        Project newProject = FakeEntityProvider.createProject(user);
        return projectRepository.save(newProject);
    }

    private ProjectResponse getNewSavedProject(Long userId) {
        ProjectRequest request = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
            NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, userId, techStacks, members
        );
        return projectService.save(userId, null, request);
    }

    private Comment createAndSaveComment(User user, Project project) {
        Comment newComment = FakeEntityProvider.createComment(user, project);
        return commentRepository.save(newComment);
    }

    @BeforeEach
    void setup() {
        members = new ArrayList<>();
        fellowMemberIds = new ArrayList<>();
        for (int i = 1; i <= MEMBER_COUNT - 1; i++) {
            Long savedUserId = createAndSaveUser().getId();
            fellowMemberIds.add(savedUserId);
            members.add(FakeDtoProvider.createFellowMemberSaveRequest(savedUserId));
        }

        user = createAndSaveUser();
        fellowMemberIds.add(0, user.getId());
        members.add(0, FakeDtoProvider.createFellowMemberSaveRequest(user.getId()));

        techStacks = new ArrayList<>();
        for (int i = 1; i <= PROJECT_SKILL_COUNT; i++) {
            Skill skill = createAndSaveSkill();
            techStacks.add(
                FakeDtoProvider.createProjectSkillSaveRequest(skill.getId())
            );
        }
    }

    @Nested
    class 프로젝트_상세_조회_테스트 {

        @Test
        void 프로젝트_상세_및_댓글_목록_조회를_성공한다() {
            // given
            User user = createAndSaveUser();
            Project project = createAndSaveProject(user);
            Comment comment = createAndSaveComment(user, project);
            CommentResponse commentResponse = CommentResponse.from(comment, true, List.of());

            // when
            ProjectResponse response = projectService.findById(project.getId());

            // then
            assertThat(response).extracting("id", "ownerId", "viewCount", "comments")
                .containsExactly(project.getId(), user.getId(), 1L, List.of(commentResponse));
        }

        @Test
        void 프로젝트_ID가_존재하지_않으면_프로젝트_상세_조회를_실패한다() {
            // given
            Long invalidId = faker.random().nextLong(Long.MAX_VALUE);

            // when
            ThrowingCallable findById = () -> projectService.findById(invalidId);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(findById)
                .withMessage(ProjectErrorCode.ID_NOT_EXISTING.getMessage());
        }
    }

    @Nested
    class 프로젝트_저장_테스트 {

        @Test
        void 필수_정보가_모두_포함되어_프로젝트_저장에_성공한다() {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId(), techStacks, members
            );

            // when
            ProjectResponse response = projectService.save(user.getId(), null, request);

            // then
            assertThat(response).extracting("name", "overview", "githubUrl", "description",
                    "ownerId")
                .containsExactly(NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId());
            assertThat(response.techStacks()).hasSize(techStacks.size());
            assertThat(response.members()).hasSize(members.size());
        }

        @ParameterizedTest(name = "[{index}] {0}이(가) 누락된 경우 실패한다.")
        @MethodSource("sixgaezzang.sidepeek.projects.util.TestParameterProvider#createProjectsWithoutRequired")
        void 작성자_Id_외_필수_정보가_누락되어_프로젝트_저장에_실패한다(
            String testMessage, String name, String overview, String githubUrl, String description,
            String message
        ) {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                name, overview, githubUrl, description, user.getId(), techStacks, members
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), null, request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }

        @Test
        void 작성자_Id가_누락되어_프로젝트_저장에_실패한다() {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, null, techStacks, members
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), null, request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(OWNER_ID_IS_NULL);
        }

        @Test
        void 작성자_Id가_로그인_Id와_불일치하여_프로젝트_저장에_실패한다() {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId() - 1, techStacks, members
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), null, request);

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(saveProject)
                .withMessage(OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.projects.util.TestParameterProvider#createProjectsOnlyInvalidRequired")
        void 유효하지_않은_필수_정보로_프로젝트_저장에_실패한다(
            String testMessage, String name, String overview, String githubUrl, String description,
            String message
        ) {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                name, overview, githubUrl, description, user.getId(), techStacks, members
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), null, request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.projects.util.TestParameterProvider#createProjectsWithInvalidOption")
        void 유효하지_않은_옵션_정보로_프로젝트_저장에_실패한다(
            String testMessage, String subName, String thumbnailUrl, String deployUrl,
            String troubleShooting,
            YearMonth startDate, YearMonth endDate, String message
        ) {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestWithOwnerIdAndOption(
                techStacks, user.getId(), subName, thumbnailUrl, deployUrl, troubleShooting,
                startDate, endDate
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), null, request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }

        @Test
        void 사용자가_로그인을_하지_않아서_프로젝트_저장에_실패한다() {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId(), techStacks, members
            );

            // when
            ThrowingCallable save = () -> projectService.save(null, null, request);

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(save)
                .withMessage(LOGIN_IS_REQUIRED);
        }

    }

    @Nested
    class 프로젝트_수정_테스트 {

        @Test
        void 로그인한_사용자가_프로젝트_회원_멤버라서_기존_프로젝트_수정에_성공한다() {
            fellowMemberIds.forEach(fellowMemberId -> {
                // given
                ProjectResponse originalProject = getNewSavedProject(user.getId());

                // when
                String newName = FakeValueProvider.createProjectName();
                String newOverview = FakeValueProvider.createOverview();
                String newGithubUrl = FakeValueProvider.createUrl();
                String newDescription = FakeValueProvider.createLongText();
                ProjectRequest newRequest = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                    newName, newOverview, newGithubUrl, newDescription, user.getId(), techStacks,
                    members
                );
                ProjectResponse savedProject = projectService.save(fellowMemberId,
                    originalProject.id(), newRequest);

                // then
                assertThat(savedProject).isNotEqualTo(originalProject);
                assertThat(savedProject).extracting("name", "overview", "githubUrl", "description")
                    .containsExactly(newName, newOverview, newGithubUrl, newDescription);
                assertThat(savedProject.techStacks()).hasSize(techStacks.size());
                assertThat(savedProject.members()).hasSize(members.size());
            });
        }

        @Test
        void 존재하지_않는_프로젝트_수정에_실패한다() {
            // given
            ProjectResponse originalProject = getNewSavedProject(user.getId());

            // when
            String newName = FakeValueProvider.createProjectName();
            String newOverview = FakeValueProvider.createOverview();
            String newGithubUrl = FakeValueProvider.createUrl();
            String newDescription = FakeValueProvider.createLongText();
            ProjectRequest newRequest = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                newName, newOverview, newGithubUrl, newDescription, user.getId(), techStacks,
                members
            );
            ThrowingCallable update = () -> projectService.save(user.getId(),
                originalProject.id() + 1, newRequest);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(update)
                .withMessage(ProjectErrorCode.ID_NOT_EXISTING.getMessage());
        }

        @Test
        void 사용자가_로그인을_하지_않아서_프로젝트_수정에_실패한다() {
            // given
            ProjectResponse originalProject = getNewSavedProject(user.getId());

            // when
            String newName = FakeValueProvider.createProjectName();
            String newOverview = FakeValueProvider.createOverview();
            String newGithubUrl = FakeValueProvider.createUrl();
            String newDescription = FakeValueProvider.createLongText();
            ProjectRequest newRequest = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                newName, newOverview, newGithubUrl, newDescription, user.getId(), techStacks,
                members
            );
            ThrowingCallable update = () -> projectService.save(null, originalProject.id(),
                newRequest);

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(update)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 로그인한_사용자가_프로젝트_회원_멤버가_아니라서_기존_프로젝트_수정에_실패한다() {
            List<Long> nonMemberIds = new ArrayList<>();
            for (int i = 1; i < 6; i++) {
                nonMemberIds.add(user.getId() + i);
            }

            nonMemberIds.forEach(nonMemberId -> {
                // given
                ProjectResponse originalProject = getNewSavedProject(user.getId());

                // when
                String newName = FakeValueProvider.createProjectName();
                String newOverview = FakeValueProvider.createOverview();
                String newGithubUrl = FakeValueProvider.createUrl();
                String newDescription = FakeValueProvider.createLongText();
                ProjectRequest newRequest = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                    newName, newOverview, newGithubUrl, newDescription, user.getId(), techStacks,
                    members
                );
                ThrowingCallable update = () -> projectService.save(nonMemberId,
                    originalProject.id(), newRequest);

                // then
                assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(update)
                    .withMessage(ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE);
            });
        }
    }

    @Nested
    class 프로젝트_삭제_테스트 {

        @Test
        void 프로젝트_소프트_삭제에_성공한다() {
            // given
            ProjectResponse project = getNewSavedProject(user.getId());

            // when
            projectService.delete(user.getId(), project.id());

            // TODO: @SQLRestriction("deleted_at IS NULL")이 안먹힌다. 왜지?
            Optional<Project> deletedProject = projectRepository.findById(project.id());
            projectService.findById(project.id());

            // then
            assertThat(deletedProject).isNotEmpty();
            assertThat(deletedProject.get().getDeletedAt()).isNotNull();
        }

        @Test
        void 로그인하지_않은_사용자라서__프로젝트_삭제에_실패한다() {
            // given
            ProjectResponse project = getNewSavedProject(user.getId());

            // when
            ThrowingCallable delete = () -> projectService.delete(null, project.id());

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(delete)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 존재하지_않는_프로젝트_삭제에_실패한다() {
            // given
            ProjectResponse project = getNewSavedProject(user.getId());

            // when
            ThrowingCallable delete = () -> projectService.delete(user.getId(), project.id() + 1);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(delete)
                .withMessage(ProjectErrorCode.ID_NOT_EXISTING.getMessage());
        }

        @Test
        void 프로젝트_작성자가_아니라서_프로젝트_삭제에_실패한다() {
            // given
            ProjectResponse project = getNewSavedProject(user.getId());

            User newUser = createAndSaveUser();

            // when
            ThrowingCallable delete = () -> projectService.delete(newUser.getId(), project.id());

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(delete)
                .withMessage(OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }

    }
}
