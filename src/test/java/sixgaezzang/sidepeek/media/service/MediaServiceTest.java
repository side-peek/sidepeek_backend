package sixgaezzang.sidepeek.media.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sixgaezzang.sidepeek.media.dto.response.MediaUploadResponse;
import sixgaezzang.sidepeek.media.util.S3Properties;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MediaServiceTest {

    @MockBean
    S3Client s3Client;

    @Autowired
    S3Properties s3Properties;

    @Autowired
    MultipartProperties multipartProperties;

    @Autowired
    MediaService fileService;

    @Nested
    class 파일_업로드_테스트 {

        @ParameterizedTest(name = "[{index}] ContentType이 {0}이고 Extension이 {1}인 경우")
        @CsvSource(value = {"image/*:.jpeg", "video/*:.mp4"}, delimiter = ':')
        void 이미지_또는_영상_파일_업로드에_성공한다(String contentType, String fileExtension) {
            // given
            MultipartFile file = makeFile(contentType, "fileName", fileExtension, "File Input Stream".getBytes());

            // when
            MediaUploadResponse response = fileService.uploadFile(file);

            // then
            assertThat(response.fileUrl()).contains(s3Properties.basePath());
            assertThat(response.fileUrl()).contains(fileExtension);
        }

        @ParameterizedTest(name = "[{index}] ContentType이 {0}이고 Extension이 {1}인 경우")
        @CsvSource(value = {"text/*:.txt", "audio/*:.aac", "font/*:.ttf"}, delimiter = ':')
        void 이미지나_영상이_아닌_파일_업로드에_실패한다(String contentType, String fileExtension) {
            // given
            MultipartFile file = makeFile(contentType, "fileName", fileExtension, "File Input Stream".getBytes());

            // when
            ThrowingCallable upload = () -> fileService.uploadFile(file);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(upload)
                .withMessage("이미지 혹은 영상 파일만 가능합니다.");
        }

        @Test
        void 파일이_null인_경우_업로드에_실패한다() {
            // given, when
            ThrowableAssert.ThrowingCallable upload = () -> fileService.uploadFile(null);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(upload)
                .withMessage("파일이 null이어서는 안됩니다.");
        }

        @Test
        void 파일이_비어있는_경우_업로드에_실패한다() {
            // given
            MultipartFile file = makeFile("image/jpeg", "fileName", ".jpeg", new byte[] {});

            // when
            ThrowingCallable upload = () -> fileService.uploadFile(file);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(upload)
                .withMessage("파일이 비어있습니다.");
        }

        @Test
        void 파일_사이즈가_최대_파일_사이즈보다_커서_업로드에_실패한다() {
            // given
            int maxSize = (int) multipartProperties.getMaxFileSize().toBytes();
            byte[] greaterThanMaxSize = new byte[maxSize + 1];
            MultipartFile file = makeFile("image/jpeg", "fileName", ".jpeg", greaterThanMaxSize);

            // when
            ThrowingCallable upload = () -> fileService.uploadFile(file);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(upload)
                .withMessage("파일 용량은 " + multipartProperties.getMaxFileSize().toMegabytes() + "MB 이하여야합니다.");
        }
    }

    private MultipartFile makeFile(String contentType, String name, String extension, byte[] size) {
        return new MockMultipartFile(name + extension,
            name + extension, contentType, size);
    }

}
