package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@Schema(description = "커서 기반 페이지네이션 결과 응답")
@Builder
public record CursorPaginationResponse<T>(

    @Schema(description = "프로젝트 목록, 없으면 빈 배열 반환")
    List<T> content,

    @Schema(description = "전체 데이터 개수")
    long totalElements,

    @Schema(description = "현재 페이지의 데이터 개수")
    int numberOfElements,

    @Schema(description = "다음 페이지 존재 여부")
    boolean hasNext
) {

    public static <T> CursorPaginationResponse from(List<T> projects,
        long totalElements,
        boolean hasNext) {
        return CursorPaginationResponse.builder()
            .content(new ArrayList<>(projects))
            .totalElements(totalElements)
            .numberOfElements(projects.size())
            .hasNext(hasNext)
            .build();
    }

}
