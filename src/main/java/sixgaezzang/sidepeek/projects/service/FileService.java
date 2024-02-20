package sixgaezzang.sidepeek.projects.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.file.File;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.repository.FileRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public void saveAll(Long projectId, List<String> overviewImageUrls) {
        List<File> overviewImages = overviewImageUrls.stream()
            .map(overviewImage -> File.builder()
                .projectId(projectId)
                .url(overviewImage)
                .type(FileType.OVERVIEW_IMAGE)
                .build())
            .toList();

        fileRepository.saveAll(overviewImages);
    }
}
