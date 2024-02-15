package sixgaezzang.sidepeek.file.service;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sixgaezzang.sidepeek.file.dto.FileUploadResponse;
import sixgaezzang.sidepeek.file.util.S3Properties;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final String FILE_EXTENSION_SEPARATOR = ".";

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    public FileUploadResponse uploadFile(MultipartFile file) {
        String fileName = createUniqueFileName(file);

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(s3Properties.baseBucket())
                .key(fileName)
                .build();

            s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FileUploadResponse.from(s3Properties.basePath() + fileName);
    }

    private String createUniqueFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);

        return UUID.randomUUID() + FILE_EXTENSION_SEPARATOR + fileExtension;
    }

}
