package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.projects.exception.message.FileErrorMessage.OVERVIEW_IMAGE_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_IS_NULL;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_OVERVIEW_IMAGE_COUNT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.File;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.repository.FileRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.projects.util.FakeEntityProvider;
import sixgaezzang.sidepeek.projects.util.FakeValueProvider;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FileServiceTest {

    @Autowired
    FileService fileService;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Nested
    class 파일_저장_및_수정_테스트 {

        static final int IMAGE_COUNT = MAX_OVERVIEW_IMAGE_COUNT / 2;
        static List<String> overLengthImageUrls;
        static List<String> imageUrls;
        Project project;
        User user;

        @BeforeEach
        void setup() {
            user = createAndSaveUser();
            project = createAndSaveProject(user);

            overLengthImageUrls = FakeValueProvider.createUrls(MAX_OVERVIEW_IMAGE_COUNT);
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
            assertThat(savedImageUrls).isEmpty();
        }

        @Test
        void 목록_개수가_최대를_넘어서_파일_목록_저장에_실패한다() {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> fileService.saveAll(project, overLengthImageUrls);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(OVERVIEW_IMAGE_OVER_MAX_COUNT);
        }

        @Test
        void 프로젝트가_null이어서_파일_목록_저장에_실패한다() {
            // given
            Project nullProject = null;

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> fileService.saveAll(nullProject, imageUrls);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(PROJECT_IS_NULL);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.projects.util.TestParameterProvider#createInvalidFileInfo")
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

        @Test
        void 기존_프로젝트_파일_목록을_지우고_파일_목록_수정에_성공한다() {
            // given
            fileService.saveAll(project, imageUrls);
            List<File> originalFiles = fileService.findAllByType(project, FileType.OVERVIEW_IMAGE);

            // when
            List<String> emptyFile = Collections.emptyList();
            fileService.saveAll(project, emptyFile);
            List<File> savedFiles = fileService.findAllByType(project, FileType.OVERVIEW_IMAGE);

            // then
            assertThat(originalFiles).isNotEqualTo(savedFiles);
            assertThat(savedFiles).hasSameSizeAs(emptyFile);
        }

        private User createAndSaveUser() {
            User newUser = FakeEntityProvider.createUser();
            return userRepository.save(newUser);
        }

        private Project createAndSaveProject(User user) {
            Project newProject = FakeEntityProvider.createProject(user);
            return projectRepository.save(newProject);
        }
    }
}
