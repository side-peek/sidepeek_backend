package sixgaezzang.sidepeek.projects.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;

@Schema(description = "프로젝트 조회 시 필터 및 페이지네이션 정보")
@Builder
public record CursorPaginationInfoRequest(

    @Schema(description = "더보기 이전 마지막으로 보여진 프로젝트 식별자(첫 페이지면 null)")
    @Nullable
    Long lastProjectId,

    @Schema(description = "더보기 이전 마지막으로 보여진 좋아요수/조회수(첫 페이지면 null)")
    @Nullable
    Long lastOrderCount,

    @Schema(description = "한 페이지내 보여질 데이터의 개수")
    @Nullable
    Integer pageSize,

    @Schema(description = "정렬 조건 [ createdAt(default), view, like ]")
    @Nullable
    SortType sort,

    @Schema(description = "출시 서비스만 보기(기본 - false)")
    @Nullable
    Boolean isReleased
) {

    public CursorPaginationInfoRequest(@Nullable Long lastProjectId, Long lastOrderCount,
        Integer pageSize, SortType sort,
        Boolean isReleased) {
        this.lastProjectId = lastProjectId;
        this.lastOrderCount = lastOrderCount;
        this.pageSize = (pageSize != null) ? pageSize : 1;
        this.sort = (sort != null) ? sort : SortType.createdAt;
        this.isReleased = (isReleased != null) ? isReleased : false;
    }

}
