package sixgaezzang.sidepeek.projects.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.projects.domain.file.File;

@Builder
public record OverviewImageSummary(
    Long id,
    String url
) {

    public static OverviewImageSummary from(File file) {
        return OverviewImageSummary.builder()
            .id(file.getId())
            .url(file.getUrl())
            .build();
    }

}
