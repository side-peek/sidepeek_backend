package sixgaezzang.sidepeek.media.dto.response;

public record MediaUploadResponse(
    String fileUrl
) {
    public static MediaUploadResponse from(String url) {
        return new MediaUploadResponse(url);
    }

}
