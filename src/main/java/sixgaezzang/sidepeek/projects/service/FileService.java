package sixgaezzang.sidepeek.projects.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.file.File;
import sixgaezzang.sidepeek.projects.domain.file.FileType;
import sixgaezzang.sidepeek.projects.dto.request.OverviewImageSaveRequest;
import sixgaezzang.sidepeek.projects.repository.FileRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
    FileRepository fileRepository;

    public void saveAll(List<OverviewImageSaveRequest> overviewImageSaveRequests) {
        List<File> overviewImages = overviewImageSaveRequests.stream()
            .map(overviewImage -> File.builder()
                .projectId(overviewImage.projectId())
                .url(overviewImage.url())
                .type(FileType.OVERVIEW_IMAGE)
                .build())
            .toList();
        
        fileRepository.saveAll(overviewImages);
    }
}
