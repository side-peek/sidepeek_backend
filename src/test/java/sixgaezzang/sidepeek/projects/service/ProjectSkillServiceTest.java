package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_CATEGORY_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_SKILL_COUNT;

import jakarta.persistence.EntityNotFoundException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import sixgaezzang.sidepeek.skill.domain.Skill;
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

        static final int PROJECT_SKILL_COUNT = MAX_PROJECT_SKILL_COUNT / 2;
        static List<ProjectSkillSaveRequest> techStacks;
        static List<ProjectSkillSaveRequest> overLengthTechStacks;
        Project project;
        User user;
        Skill skill;

        private static Stream<Arguments> createInvalidProjectSkillInfo() {
            return Stream.of(
                Arguments.of("기술 스택 카테고리를 누락하는 경우", null,
                    "기술 스택 카테고리를 입력해주세요."),
                Arguments.of("기술 스택 카테고리가 최대 길이를 넘는 경우", "C".repeat(MAX_CATEGORY_LENGTH + 1),
                    "기술 스택 카테고리는 " + MAX_CATEGORY_LENGTH + "자 이하여야 합니다.")
            );
        }

        @Test
        void 프로젝트_기술_스택_목록_저장에_성공한다() {
            // given, when
            List<ProjectSkillSummary> savedTechStacks = projectSkillService.saveAll(project, techStacks);

            // then
            assertThat(savedTechStacks).hasSize(PROJECT_SKILL_COUNT);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 빈_기술_스택_목록_저장에_실패한다(List<ProjectSkillSaveRequest> emptyTechStacks) {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> projectSkillService.saveAll(project, emptyTechStacks);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage("기술 스택들을 입력해주세요.");
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

        @Test
        void 목록_개수가_최대를_넘어서_기술_스택_목록_저장에_실패한다() {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> projectSkillService.saveAll(project, overLengthTechStacks);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage("기술 스택은 " + MAX_PROJECT_SKILL_COUNT + "개 미만이어야 합니다.");
        }

        @BeforeEach
        void setup() {
            overLengthTechStacks = new ArrayList<>();
            for (int i = 1; i <= MAX_PROJECT_SKILL_COUNT; i++) {
                Skill skill = createSkillWithName("skill" + i);
                overLengthTechStacks.add(
                    new ProjectSkillSaveRequest(skill.getId(), "category" + i)
                );
            }

            user = createUser();
            project = createProject(user);
            techStacks = overLengthTechStacks.subList(0, PROJECT_SKILL_COUNT);
            skill = createSkillWithName("skill");
        }

        @ParameterizedTest
        @MethodSource("createInvalidProjectSkillInfo")
        void 기술_스택_카테고리가_유효하지_않아_기술_스택_목록_저장에_실패한다(
            String testMessage, String category, String message
        ) {
            // given
            List<ProjectSkillSaveRequest> techStacksWithInvalidSkill = new ArrayList<>(techStacks);
            techStacksWithInvalidSkill.add(
                new ProjectSkillSaveRequest(skill.getId(), category)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> projectSkillService.saveAll(project, techStacksWithInvalidSkill);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(message);
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

        private Skill createSkillWithName(String name) {
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

        @Test
        void 존재하지_않는_기술_스택_Id로_기술_스택_목록_저장에_실패한다() {
            // given
            List<ProjectSkillSaveRequest> techStacksWithNonExistSkill = new ArrayList<>(techStacks);
            techStacksWithNonExistSkill.add(
                new ProjectSkillSaveRequest(skill.getId() + 1L, "category")
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> projectSkillService.saveAll(project, techStacksWithNonExistSkill);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(saveAll)
                .withMessage("Skill Id에 해당하는 스킬이 없습니다.");
        }

        @Test
        void 기술_스택_Id가_누락되어_기술_스택_목록_저장에_실패한다() {
            // given
            List<ProjectSkillSaveRequest> techStacksWithNonExistSkill = new ArrayList<>(techStacks);
            techStacksWithNonExistSkill.add(
                new ProjectSkillSaveRequest(null, "category")
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> projectSkillService.saveAll(project, techStacksWithNonExistSkill);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage("기술 스택의 스택 Id를 입력해주세요");
        }

    }

}
