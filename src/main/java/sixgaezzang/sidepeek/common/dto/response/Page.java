package sixgaezzang.sidepeek.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "페이징 정보")
@Builder
public record Page<T>(
    @Schema(description = "데이터 목록, 없으면 빈 배열 반환")
    List<T> data,
    @Schema(description = "전체 페이지 수", example = "3")
    int totalPages,
    @Schema(description = "전체 데이터 수", example = "10")
    long totalElements,
    @Schema(description = "현재 페이지 번호", example = "0", defaultValue = "0")
    int pageNumber,
    @Schema(description = "페이지 크기", example = "12", defaultValue = "12")
    int pageSize,
    @Schema(description = "현재 페이지의 데이터 수", example = "10")
    int numberOfElementsOnCurrentPage,
    @Schema(description = "현재 페이지의 데이터가 비어있는지 여부", example = "false")
    boolean isEmpty
) {

    public static <T> Page<T> from(org.springframework.data.domain.Page<T> page) {
        return Page.<T>builder()
            .data(page.getContent())
            .totalPages(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .numberOfElementsOnCurrentPage(page.getNumberOfElements())
            .isEmpty(page.isEmpty())
            .build();
    }
}
