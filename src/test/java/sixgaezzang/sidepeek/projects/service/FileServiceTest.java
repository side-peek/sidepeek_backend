package sixgaezzang.sidepeek.projects.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.repository.FileRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FileServiceTest {

    @Autowired
    FileService fileService;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Nested
    class 파일_저장_테스트 {

        static final int IMAGE_COUNT = 5;
        static List<String> imageUrls;
        Project project;

        @BeforeEach
        void setup() {
            project = createProject();

            imageUrls = new ArrayList<>();
            for (int i = 0; i < IMAGE_COUNT; i++) {
                imageUrls.add("https://sidepeek.image/image" + i);
            }
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
        void 빈_파일_목록_저장은_무시된다(List<String> emptyImageUrls) {
            // given, when
            List<OverviewImageSummary> savedImageUrls = fileService.saveAll(project, emptyImageUrls);

            // then
            assertThat(savedImageUrls).isNull();
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

        private Project createProject() {
            Project project = Project.builder()
                .name("Project Name")
                .subName("Project SubName")
                .overview("Project Overview")
                .ownerId(1L)
                .githubUrl("https://github.com")
                .description("Project description")
                .thumbnailUrl("https://sidepeek.image/thumbnail")
                .deployUrl("https://deploy.com")
                .startDate(YearMonth.of(2023, 9))
                .endDate(YearMonth.of(2024, 3))
                .troubleshooting("Project TroubleShooting")
                .build();

            return projectRepository.save(project);
        }

    }
}
