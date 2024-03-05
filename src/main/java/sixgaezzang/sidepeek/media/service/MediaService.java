package sixgaezzang.sidepeek.media.service;

import static sixgaezzang.sidepeek.media.exception.message.MediaErrorMessage.CANNOT_READ_FILE;
import static sixgaezzang.sidepeek.media.util.MediaConstant.FILE_EXTENSION_SEPARATOR;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sixgaezzang.sidepeek.common.util.ValidationUtils;
import sixgaezzang.sidepeek.media.dto.response.MediaUploadResponse;
import sixgaezzang.sidepeek.config.properties.S3Properties;
import sixgaezzang.sidepeek.media.util.validation.MediaValidator;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final S3Client s3Client;
    private final S3Properties s3Properties;
    private final MultipartProperties multipartProperties;

    public MediaUploadResponse uploadFile(Long loginId, MultipartFile file) {
        ValidationUtils.validateLoginId(loginId);
        MediaValidator.validateFile(file, multipartProperties.getMaxFileSize());
        String fileName = createUniqueFileName(file);

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(s3Properties.baseBucket())
                .key(fileName)
                .build();

            s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new IllegalStateException(CANNOT_READ_FILE);
        }

        return MediaUploadResponse.from(s3Properties.basePath() + fileName);
    }

    private String createUniqueFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);

        return UUID.randomUUID() + fileExtension;
    }

}
