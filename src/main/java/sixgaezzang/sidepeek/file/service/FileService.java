package sixgaezzang.sidepeek.file.service;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.unit.DataSize;
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
    private static final String IMAGE_PREFIX = "image";
    private static final String VIDEO_PREFIX = "video";

    private final S3Client s3Client;
    private final S3Properties s3Properties;
    private final MultipartProperties multipartProperties;

    public FileUploadResponse uploadFile(MultipartFile file) {
        validateFile(file);
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

    private void validateFile(MultipartFile file) {
        Assert.notNull(file, "파일이 null이어서는 안됩니다.");
        Assert.isTrue(!file.isEmpty(), "파일이 비어있습니다.");

        DataSize fileMaxSize = multipartProperties.getMaxFileSize();
        Assert.isTrue(file.getSize() <= fileMaxSize.toBytes(), "파일 용량은 " + fileMaxSize.toMegabytes() + "MB 이하여야합니다.");

        String contentType = file.getContentType();
        Assert.isTrue(contentType.contains(IMAGE_PREFIX) || contentType.contains(VIDEO_PREFIX), "이미지 혹은 영상 파일만 가능합니다.");
    }

}
