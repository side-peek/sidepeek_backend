package sixgaezzang.sidepeek.projects.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.common.util.ValidationUtils;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.file.File;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.response.OverviewImageSummary;
import sixgaezzang.sidepeek.projects.repository.FileRepository;
import sixgaezzang.sidepeek.projects.util.validation.FileValidator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public List<OverviewImageSummary> saveAll(Project project, List<String> overviewImageUrls) {
        if (!ValidationUtils.isNotNullOrEmpty(overviewImageUrls)) {
            return null;
        }

        FileValidator.validateFiles(overviewImageUrls);

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

}
