package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OWNER_ID_IS_NULL;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_SKILL_COUNT;

import jakarta.persistence.EntityNotFoundException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
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
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.request.MemberSaveRequest;
import sixgaezzang.sidepeek.projects.dto.request.ProjectRequest;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.exception.ProjectErrorCode;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
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

    @Autowired
    ProjectService projectService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    ProjectRepository projectRepository;

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

    @Nested
    class 프로젝트_상세_조회_테스트 {

        @Test
        void 프로젝트_상세_조회를_성공한다() {
            // given
            User user = createAndSaveUser();
            Project project = createAndSaveProject(user);

            // when
            ProjectResponse response = projectService.findById(project.getId());

            // then
            assertThat(response).extracting("id", "ownerId", "viewCount")
                .containsExactly(project.getId(), user.getId(), 1L);
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
        static final int MEMBER_COUNT = MAX_MEMBER_COUNT / 2;
        static final int PROJECT_SKILL_COUNT = MAX_PROJECT_SKILL_COUNT / 2;
        static List<MemberSaveRequest> members;
        static List<ProjectSkillSaveRequest> techStacks;
        static String NAME = FakeValueProvider.createProjectName();
        static String OVERVIEW = FakeValueProvider.createOverview();
        static String GITHUB_URL = FakeValueProvider.createUrl();
        static String DESCRIPTION = FakeValueProvider.createLongText();
        User user;

        @BeforeEach
        void setup() {
            members = new ArrayList<>();
            for (int i = 1; i <= MEMBER_COUNT - 1; i++) {
                members.add(
                    FakeDtoProvider.createNonFellowMemberSaveRequest()
                );
            }

            user = createAndSaveUser();
            members.add(0, FakeDtoProvider.createFellowMemberSaveRequest(user.getId()));

            techStacks = new ArrayList<>();
            for (int i = 1; i <= PROJECT_SKILL_COUNT; i++) {
                Skill skill = createAndSaveSkill();
                techStacks.add(
                    FakeDtoProvider.createProjectSkillSaveRequest(skill.getId())
                );
            }
        }

        @Test
        void 필수_정보가_모두_포함되어_프로젝트_저장에_성공한다() {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId(), techStacks, members
            );

            // when
            ProjectResponse response = projectService.save(user.getId(), request);

            // then
            assertThat(response).extracting("name", "overview", "githubUrl", "description", "ownerId")
                .containsExactly(NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId());
            assertThat(response.techStacks()).hasSize(techStacks.size());
        }

        @ParameterizedTest(name = "[{index}] {0}이(가) 누락된 경우 실패한다.")
        @MethodSource("sixgaezzang.sidepeek.projects.util.TestParameterProvider#createProjectsWithoutRequired")
        void 작성자_Id_외_필수_정보가_누락되어_프로젝트_저장에_실패한다(
            String testMessage, String name, String overview, String githubUrl, String description, String message
        ) {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                name, overview, githubUrl, description, user.getId(), techStacks, members
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), request);

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
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(OWNER_ID_IS_NULL);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.projects.util.TestParameterProvider#createProjectsOnlyInvalidRequired")
        void 유효하지_않은_필수_정보로_프로젝트_저장에_실패한다(
            String testMessage, String name, String overview, String githubUrl, String description, String message
        ) {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestOnlyRequired(
                name, overview, githubUrl, description, user.getId(), techStacks, members
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.projects.util.TestParameterProvider#createProjectsWithInvalidOption")
        void 유효하지_않은_옵션_정보로_프로젝트_저장에_실패한다(
            String testMessage, String subName, String thumbnailUrl, String deployUrl, String troubleShooting,
            YearMonth startDate, YearMonth endDate, String message
        ) {
            // given
            ProjectRequest request = FakeDtoProvider.createProjectSaveRequestWithOwnerIdAndOption(
                techStacks, user.getId(), subName, thumbnailUrl, deployUrl, troubleShooting, startDate, endDate
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }
    }
}
