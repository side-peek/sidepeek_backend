package sixgaezzang.sidepeek.media.util.validation;

import static sixgaezzang.sidepeek.media.exception.message.MediaErrorMessage.FILE_IS_EMPTY;
import static sixgaezzang.sidepeek.media.exception.message.MediaErrorMessage.FILE_IS_INVALID;
import static sixgaezzang.sidepeek.media.util.MediaConstant.IMAGE_PREFIX;
import static sixgaezzang.sidepeek.media.util.MediaConstant.VIDEO_PREFIX;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MediaValidator {
    public static void validateFile(MultipartFile file, DataSize fileMaxSize) {
        Assert.isTrue(Objects.nonNull(file) && !file.isEmpty(), FILE_IS_EMPTY);
        Assert.isTrue(file.getSize() <= fileMaxSize.toBytes(),
            "파일 용량은 " + fileMaxSize.toMegabytes() + "MB 이하여야합니다.");

        String contentType = file.getContentType();
        Assert.isTrue(Objects.nonNull(contentType) && isImageOrVideoType(contentType),
            FILE_IS_INVALID);
    }

    private static boolean isImageOrVideoType(String contentType) {
        return contentType.contains(IMAGE_PREFIX) || contentType.contains(VIDEO_PREFIX);
    }
}
