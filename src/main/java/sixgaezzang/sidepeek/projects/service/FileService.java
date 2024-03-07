package sixgaezzang.sidepeek.projects.service;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.isNotNullOrEmpty;
import static sixgaezzang.sidepeek.projects.util.validation.FileValidator.validateFiles;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateProject;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.File;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.repository.FileRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public List<OverviewImageSummary> saveAll(Project project, List<String> overviewImageUrls) {
        validateProject(project);
        if (fileRepository.existsByProject(project)) {
            fileRepository.deleteAllByProjectAndType(project, FileType.OVERVIEW_IMAGE);
        }

        if (!isNotNullOrEmpty(overviewImageUrls)) {
            return Collections.emptyList();
        }

        validateFiles(overviewImageUrls);
        List<File> overviewImages = overviewImageUrls.stream()
            .map(
                overviewImage -> File.builder()
                    .project(project)
                    .url(overviewImage)
                    .type(FileType.OVERVIEW_IMAGE)
                    .build()
            ).toList();

        fileRepository.saveAll(overviewImages);

        return overviewImages.stream()
            .map(OverviewImageSummary::from)
            .toList();
    }

    public List<File> findAllByType(Project project, FileType fileType) {
        return fileRepository.findAllByProjectAndType(project, fileType);
    }

}
