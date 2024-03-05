package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import jakarta.persistence.EntityNotFoundException;
import java.time.YearMonth;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
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

    @Nested
    class 프로젝트_상세_조회_테스트 {

        @Test
        void 프로젝트_상세_조회를_성공한다() {
            // given
            User user = createUser();
            Project project = createProject(user);

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

}
