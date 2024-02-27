package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.projects.service.FileServiceTest.파일_저장_테스트.IMAGE_COUNT;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectSkillServiceTest {

    static final Faker faker = new Faker();

    @Autowired
    ProjectSkillService projectSkillService;
    @Autowired
    ProjectSkillRepository projectSkillRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;

    @Nested
    class 프로젝트_기술_스택_목록_저장_테스트 {

        static final int SKILL_COUNT = 5;
        static List<ProjectSkillSaveRequest> techStacks;
        Project project;
        User user;

        @BeforeEach
        void setup() {
            project = createProject();

            techStacks = new ArrayList<>();
            for (int i = 0; i < IMAGE_COUNT; i++) {
                techStacks.add("https://sidepeek.image/image" + i);
            }
        }

        @Test
        void 프로젝트_기술_스택_목록_저장에_성공한다() {
            // given, when
            List<ProjectSkillSummary> savedTechStacks = projectSkillService.saveAll(project, techStacks);

            // then
            assertThat(savedTechStacks).hasSize(IMAGE_COUNT);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 빈_프로젝트_기술_스택_목록_저장_무시된다(List<ProjectSkillSaveRequest> emptyTechStacks) {
            // given, when
            List<ProjectSkillSummary> savedTechStacks = projectSkillService.saveAll(project, emptyTechStacks);

            // then
            assertThat(savedTechStacks).isNull();
        }

        @Test
        void 프로젝트가_null이어서_프로젝트_기술_스택_목록_저장에_실패한다() {
            // given
            Project nullProject = null;

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> projectSkillService.saveAll(nullProject, techStacks);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage("프로젝트가 null 입니다.");
        }


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

        private Project createProject(User user) {
            String name = faker.internet().domainName();
            String subName = faker.internet().domainWord();
            String overview = faker.lorem().sentence();
            String thumbnailUrl = faker.internet().url();
            String githubUrl = faker.internet().url();
            YearMonth startDate = YearMonth.now();
            YearMonth endDate = startDate.plusMonths(3);
            String description = faker.lorem().sentences(10).toString();

            Project project = Project.builder()
                .name(name)
                .subName(subName)
                .overview(overview)
                .thumbnailUrl(thumbnailUrl)
                .githubUrl(githubUrl)
                .startDate(startDate)
                .endDate(endDate)
                .ownerId(user.getId())
                .description(description)
                .build();

            return projectRepository.save(project);
        }

    }
}
