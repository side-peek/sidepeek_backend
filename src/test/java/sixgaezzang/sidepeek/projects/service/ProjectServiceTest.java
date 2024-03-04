package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.like.repository.LikeRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.exception.ProjectErrorCode;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;
import sixgaezzang.sidepeek.users.domain.Password;
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

    @Autowired
    LikeRepository likeRepository;

    private User createUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 40, true, true, true);
        String nickname = faker.internet().username();

        User user = User.builder()
            .email(email)
            .password(new Password(password, new BCryptPasswordEncoder()))
            .nickname(nickname)
            .build();
        return userRepository.save(user);
    }

    private Skill createSkill() {
        String name = faker.computer().macos();
        String iconImageUrl = faker.internet().url();

        Skill skill = Skill.builder()
            .name(name)
            .iconImageUrl(iconImageUrl)
            .build();
        return skillRepository.save(skill);
    }

    private Project createProject(User user, String deployUrl) {
        String name = faker.internet().domainName();
        String subName = faker.internet().domainWord();
        String overview = faker.lorem().sentence();
        String thumbnailUrl = faker.internet().url();
        String githubUrl = faker.internet().url();
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(3);
        String description = faker.lorem().sentences(10).toString();

        Project project = Project.builder()
            .name(name)
            .subName(subName)
            .overview(overview)
            .thumbnailUrl(thumbnailUrl)
            .githubUrl(githubUrl)
            .deployUrl(deployUrl != null ? deployUrl : null)
            .startDate(startDate)
            .endDate(endDate)
            .ownerId(user.getId())
            .description(description)
            .build();

        return projectRepository.save(project);
    }

    @Nested
    class 프로젝트_상세_조회_테스트 {

        @Test
        void 프로젝트_상세_조회를_성공한다() {
            // given
            User user = createUser();
            Project project = createProject(user, null);

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
    class 프로젝트_전체_조회_테스트 {

        User user1, user2;
        Project project1, project2;
        String deployUrl = faker.internet().url();

        @BeforeEach
        void setUp() {
            user1 = createUser();
            user2 = createUser();

            project1 = createProject(user1, deployUrl);
            project2 = createProject(user1, null);
        }

        @Test
        void 전체_프로젝트를_최신순으로_조회할_수_있다() {
            // when
            List<ProjectListResponse> responses = projectService.findAll(null, "createdAt", false);

            // then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).id()).isEqualTo(project1.getId());
            assertThat(responses.get(1).id()).isEqualTo(project2.getId());
        }

        @Test
        void 전체_프로젝트를_조회순으로_조회할_수_있다() {
            // given
            projectService.findById(project1.getId());

            // when
            List<ProjectListResponse> responses = projectService.findAll(null, "view", false);

            // then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).id()).isEqualTo(project1.getId());
            assertThat(responses.get(1).id()).isEqualTo(project2.getId());
        }

        @Test
        void 전체_프로젝트를_좋아요순으로_조회할_수_있다() {
            // TODO: 좋아요 생성/취소가 완료 되면 추가할 예정입니다..!
        }

        @Test
        void 전체_프로젝트중_출시_서비스만_조회할_수_있다() {
            // when

            List<ProjectListResponse> responses = projectService.findAll(null, "createdAt", true);

            // then
            assertThat(responses).hasSize(1);
            assertThat(responses.get(0).id()).isEqualTo(project1.getId());
        }

        @Test
        void 프로젝트_전체_조회_시_로그인한_사용자가_좋아요한_프로젝트를_확인할_수_있다() {
            // TODO: 좋아요 생성/취소가 완료 되면 추가할 예정입니다..!
        }


    }

}
