package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.file.File;

@Schema(description = "프로젝트 레이아웃 이미지 URL 정보")
@Builder
public record OverviewImageSummary(
    @Schema(description = "프로젝트 레이아웃 이미지 URL 식별자", example = "1")
    Long id,
    @Schema(description = "프로젝트 레이아웃 이미지 URL", example = "https://sidepeek.image/img1.jpg")
    String url
) {

    public static OverviewImageSummary from(File file) {
        return OverviewImageSummary.builder()
            .id(file.getId())
            .url(file.getUrl())
            .build();
    }

}
