package sixgaezzang.sidepeek.projects.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@Schema(description = "커서 기반 페이지네이션 결과 응답")
@Builder
public record CursorPaginationResponse<T>(

    @Schema(description = "프로젝트 목록")
    List<T> content,

    // TODO: 검색된 전체 데이터의 개수
//    @Schema(description = "전체 데이터 개수")
//    Integer totalElements,

    @Schema(description = "현재 페이지의 데이터 개수")
    Integer numberOfElements,

    @Schema(description = "다음 페이지 존재 여부")
    boolean hasNext
) {

    public static <T> CursorPaginationResponse from(List<T> projects,
        boolean hasNext) {
        return CursorPaginationResponse.builder()
            .content(new ArrayList<>(projects))
//            .totalElements()
            .numberOfElements(projects.size())
            .hasNext(hasNext)
            .build();
    }

}
