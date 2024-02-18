package sixgaezzang.sidepeek.file.dto.response;

public record FileUploadResponse(
    String fileUrl
) {
    public static FileUploadResponse from(String url) {
        return new FileUploadResponse(url);
    }

}
