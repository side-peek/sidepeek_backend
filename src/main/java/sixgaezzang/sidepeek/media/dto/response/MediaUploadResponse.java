package sixgaezzang.sidepeek.media.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "미디어(파일) 업로드 응답")
public record MediaUploadResponse(
    @Schema(description = "파일 URL", example = "https://sidepeek.s3.ap-northeast-2.amazonaws.com/2024/03/05/20/17/00/1.jpg")
    String fileUrl
) {

    public static MediaUploadResponse from(String url) {
        return new MediaUploadResponse(url);
    }

}
