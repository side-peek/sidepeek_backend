package sixgaezzang.sidepeek.projects.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;

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
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.repository.FileRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FileServiceTest {
    static final Faker faker = new Faker();

    @Autowired
    FileService fileService;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Nested
    class 파일_저장_테스트 {

        static final int IMAGE_COUNT = MAX_OVERVIEW_IMAGE_COUNT / 2;
        static List<String> overLengthImageUrls;
        static List<String> imageUrls;
        Project project;
        User user;

        @BeforeEach
        void setup() {
            overLengthImageUrls = new ArrayList<>();
            for (int i = 1; i <= MAX_OVERVIEW_IMAGE_COUNT; i++) {
                overLengthImageUrls.add("https://sidepeek.image/image" + i);
            }

            user = createUser();
            project = createProject(user);
            imageUrls = overLengthImageUrls.subList(0, IMAGE_COUNT);
        }

        @Test
        void 파일_목록_저장에_성공한다() {
            // given, when
            List<OverviewImageSummary> savedImageUrls = fileService.saveAll(project, imageUrls);

            // then
            assertThat(savedImageUrls).hasSize(IMAGE_COUNT);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 빈_파일_목록_저장은_무시되어_성공한다(List<String> emptyImageUrls) {
            // given, when
            List<OverviewImageSummary> savedImageUrls = fileService.saveAll(project, emptyImageUrls);

            // then
            assertThat(savedImageUrls).isNull();
        }

        @Test
        void 목록_개수가_최대를_넘어서_파일_목록_저장에_실패한다() {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> fileService.saveAll(project, overLengthImageUrls);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage("프로젝트 레이아웃 이미지 수는 " + MAX_OVERVIEW_IMAGE_COUNT + "개 미만이어야 합니다.");
        }

        @Test
        void 프로젝트가_null이어서_파일_목록_저장에_실패한다() {
            // given
            Project nullProject = null;

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> fileService.saveAll(nullProject, imageUrls);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage("프로젝트가 null 입니다.");
        }

        private static Stream<Arguments> createInvalidFileInfo() {
            return Stream.of(
                Arguments.of("프로젝트 레이아웃 이미지 URL 형식이 올바르지 않은 경우",
                    "not url pattern", "프로젝트 레이아웃 이미지 URL 형식이 올바르지 않습니다."),
                Arguments.of("프로젝트 레이아웃 이미지 URL이 최대 길이를 넘는 경우",
                    "https://sidepeek.file/" + "f".repeat(MAX_TEXT_LENGTH),
                    "프로젝트 레이아웃 이미지 URL은 " + MAX_TEXT_LENGTH + "자 이하여야 합니다.")
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("createInvalidFileInfo")
        void 파일_정보가_유효하지_않아_파일_목록_저장에_실패한다(String testMessage, String fileUrl, String message) {
            // given
            List<String> imageUrlsWithInvalidUrl = new ArrayList<>(imageUrls);
            imageUrlsWithInvalidUrl.add(fileUrl);

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> fileService.saveAll(project, imageUrlsWithInvalidUrl);

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
