package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_NAME_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_PROJECT_SKILL_COUNT;
import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import jakarta.persistence.EntityNotFoundException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.request.MemberSaveRequest;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSaveRequest;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSkillSaveRequest;
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
        if (nickname.length() > MAX_NICKNAME_LENGTH) {
            nickname = nickname.substring(nickname.length() - MAX_NICKNAME_LENGTH);
        }

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

    @Nested
    class 프로젝트_저장_테스트 {
        static final int MEMBER_COUNT = MAX_PROJECT_SKILL_COUNT / 2;
        static final int PROJECT_SKILL_COUNT = MAX_PROJECT_SKILL_COUNT / 2;
        static final int IMAGE_COUNT = MAX_OVERVIEW_IMAGE_COUNT / 2;
        static List<MemberSaveRequest> members;
        static List<ProjectSkillSaveRequest> techStacks;
        static List<String> imageUrls;
        static String NAME = "Project Name";
        static String OVERVIEW = "Project Overview";
        static String GITHUB_URL = "https://github.com/code";
        static String DESCRIPTION = "Project Description";
        User user;

        private static ProjectSaveRequest createProjectSaveRequestOnlyRequired(
            String name, String overview, String githubUrl, String description, Long ownerId,
            List<ProjectSkillSaveRequest> techStacks
        ) {
            return new ProjectSaveRequest(name, overview, ownerId, githubUrl, description,
                techStacks, null, null, null, null,
                null, null, null, null);
        }

        private static ProjectSaveRequest createProjectSaveRequestWithOwnerIdAndOption(Long ownerId, String subName,
                                                                                       String thumbnailUrl,
                                                                                       String deployUrl,
                                                                                       String troubleShooting,
                                                                                       YearMonth startDate,
                                                                                       YearMonth endDate) {
            return new ProjectSaveRequest(NAME, OVERVIEW, ownerId, GITHUB_URL, DESCRIPTION,
                techStacks, subName, thumbnailUrl, deployUrl, startDate, endDate, troubleShooting, null, null);
        }

        private static Stream<Arguments> createProjectsWithoutRequiredArgument() {
            return Stream.of(
                Arguments.of("프로젝트 이름",
                    null, OVERVIEW, GITHUB_URL, DESCRIPTION, "프로젝트 제목을 입력해주세요."),
                Arguments.of("프로젝트 개요",
                    NAME, null, GITHUB_URL, DESCRIPTION, "프로젝트 개요를 입력해주세요."),
                Arguments.of("프로젝트 깃허브 url",
                    NAME, OVERVIEW, null, DESCRIPTION, "프로젝트 Github URL을 입력해주세요."),
                Arguments.of("프로젝트 기능 설명",
                    NAME, OVERVIEW, GITHUB_URL, null, "프로젝트 기능 설명을 입력해주세요.")
            );
        }

        private static Stream<Arguments> createProjectsOnlyInvalidRequiredArgument() {
            return Stream.of(
                Arguments.of("프로젝트 이름이 최대 길이를 넘는 경우",
                    "N".repeat(MAX_PROJECT_NAME_LENGTH + 1), OVERVIEW, GITHUB_URL, DESCRIPTION,
                    "프로젝트 제목은 " + MAX_PROJECT_NAME_LENGTH + "자 이하여야 합니다."),
                Arguments.of("프로젝트 개요가 최대 길이를 넘는 경우",
                    NAME, "O".repeat(MAX_OVERVIEW_LENGTH + 1), GITHUB_URL, DESCRIPTION,
                    "프로젝트 개요는 " + MAX_OVERVIEW_LENGTH + "자 이하여야 합니다."),
                Arguments.of("프로젝트 깃허브 url 형식이 올바르지 않은 경우",
                    NAME, OVERVIEW, "not url pattern", DESCRIPTION,
                    "프로젝트 Github URL 형식이 유효하지 않습니다."),
                Arguments.of("프로젝트 깃허브 url이 최대 길이를 넘는 경우",
                    NAME, OVERVIEW, GITHUB_URL + "u".repeat(MAX_TEXT_LENGTH), DESCRIPTION,
                    "프로젝트 Github URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다."),
                Arguments.of("프로젝트 기능 설명이 최대 길이를 넘는 경우",
                    NAME, OVERVIEW, GITHUB_URL, "D".repeat(MAX_TEXT_LENGTH + 1),
                    "프로젝트 기능 설명은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.")
            );
        }

        private static Stream<Arguments> createProjectsWithInvalidOptionArgument() {
            return Stream.of(
                Arguments.of("프로젝트 부제목이 최대 길이를 넘는 경우",
                    "S".repeat(MAX_PROJECT_NAME_LENGTH + 1), null, null, null, null, null,
                    "프로젝트 부제목은 " + MAX_PROJECT_NAME_LENGTH + "자 이하여야 합니다."),
                Arguments.of("프로젝트 썸네일 url 형식이 올바르지 않은 경우",
                    null, "not url pattern", null, null, null, null,
                    "프로젝트 썸네일 이미지 URL 형식이 유효하지 않습니다."),
                Arguments.of("프로젝트 썸네일 url이 최대 길이를 넘는 경우",
                    null, GITHUB_URL + "u".repeat(MAX_TEXT_LENGTH), null, null, null, null,
                    "프로젝트 썸네일 이미지 URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다."),
                Arguments.of("프로젝트 배포 url 형식이 올바르지 않은 경우",
                    null, null, "not url pattern", null, null, null,
                    "프로젝트 배포 URL 형식이 유효하지 않습니다."),
                Arguments.of("프로젝트 배포 url이 최대 길이를 넘는 경우",
                    null, null, GITHUB_URL + "u".repeat(MAX_TEXT_LENGTH), null, null, null,
                    "프로젝트 배포 URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다."),
                Arguments.of("프로젝트 트러블 슈팅 내용이 최대 길이를 넘는 경우",
                    null, null, null, "T".repeat(MAX_TEXT_LENGTH + 1), null, null,
                    "프로젝트 트러블 슈팅 설명은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다."),
                Arguments.of("프로젝트 시작/끝 기간 중 하나만 누락된 경우",
                    null, null, null, null, null, YearMonth.of(2024, 2),
                    "프로젝트 기간은 시작 날짜와 종료 날짜가 모두 기입되어야 합니다."),
                Arguments.of("프로젝트 시작/끝 기간 순서가 안맞는 경우",
                    null, null, null, null, YearMonth.of(2024, 2), YearMonth.of(2023, 1),
                    "시작 날짜가 종료 날짜와 같거나 종료 날짜보다 이전이어야합니다.")
            );
        }

        @BeforeEach
        void setup() {
            members = new ArrayList<>();
            for (int i = 1; i <= MEMBER_COUNT; i++) {
                User savedUser = createUser();
                members.add(
                    new MemberSaveRequest(savedUser.getId(), null, "role" + i)
                );
                members.add(
                    new MemberSaveRequest(null, "nonFellowMember" + i, "role" + i)
                );
            }

            user = createUser();
            members.add(
                new MemberSaveRequest(user.getId(), null, "role0")
            );

            techStacks = new ArrayList<>();
            for (int i = 1; i <= PROJECT_SKILL_COUNT; i++) {
                Skill skill = createSkillWithName("skill" + i);
                techStacks.add(
                    new ProjectSkillSaveRequest(skill.getId(), "category" + i)
                );
            }

            imageUrls = new ArrayList<>();
            for (int i = 1; i <= IMAGE_COUNT; i++) {
                imageUrls.add("https://sidepeek.image/image" + i);
            }
        }

        @Test
        void 필수_정보가_모두_포함되어_프로젝트_저장에_성공한다() {
            // given
            ProjectSaveRequest request =
                createProjectSaveRequestOnlyRequired(NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId(), techStacks);

            // when
            ProjectResponse response = projectService.save(request);

            // then
            assertThat(response).extracting("name", "overview", "githubUrl", "description", "ownerId")
                .containsExactly(NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId());
            assertThat(response.techStacks()).hasSize(techStacks.size());
        }

        @ParameterizedTest(name = "[{index}] {0}이(가) 누락된 경우 실패한다.")
        @MethodSource("createProjectsWithoutRequiredArgument")
        void 작성자_Id_외_필수_정보가_누락되어_프로젝트_저장에_실패한다(String testMessage, String name, String overview,
                                                String githubUrl, String description, String message) {
            // given
            ProjectSaveRequest request =
                createProjectSaveRequestOnlyRequired(name, overview, githubUrl, description, user.getId(), techStacks);

            // when
            ThrowingCallable saveProject = () -> projectService.save(request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }

        @Test
        void 작성자_Id가_누락되어_프로젝트_저장에_실패한다() {
            // given
            ProjectSaveRequest request =
                createProjectSaveRequestOnlyRequired(NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, null, techStacks);

            // when
            ThrowingCallable saveProject = () -> projectService.save(request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage("프로젝트 게시글 작성자 Id를 입력해주세요.");
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("createProjectsOnlyInvalidRequiredArgument")
        void 유효하지_않은_필수_정보로_프로젝트_저장에_실패한다(String testMessage, String name, String overview,
                                          String githubUrl, String description, String message) {
            // given
            ProjectSaveRequest request =
                createProjectSaveRequestOnlyRequired(name, overview, githubUrl, description, user.getId(), techStacks);

            // when
            ThrowingCallable saveProject = () -> projectService.save(request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("createProjectsWithInvalidOptionArgument")
        void 유효하지_않은_옵션_정보로_프로젝트_저장에_실패한다(String testMessage, String subName, String thumbnailUrl,
                                          String deployUrl, String troubleShooting, YearMonth startDate,
                                          YearMonth endDate, String message) {
            // given
            ProjectSaveRequest request =
                createProjectSaveRequestWithOwnerIdAndOption(user.getId(), subName, thumbnailUrl, deployUrl,
                    troubleShooting, startDate, endDate);

            // when
            ThrowingCallable saveProject = () -> projectService.save(request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }
    }
}
