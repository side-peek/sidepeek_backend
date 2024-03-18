package sixgaezzang.sidepeek.projects.dto.request;

import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.IS_RELEASED_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.LAST_ORDER_COUNT_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.LAST_PROJECT_ID_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.PAGE_SIZE_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.SORT_DESCRIPTION;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;

@Schema(description = "프로젝트 조회 시 필터 및 페이지네이션 정보")
@Builder
public record CursorPaginationInfoRequest(

    @Schema(description = LAST_PROJECT_ID_DESCRIPTION)
    @Nullable
    Long lastProjectId,

    @Schema(description = LAST_ORDER_COUNT_DESCRIPTION)
    @Nullable
    Long lastOrderCount,

    @Schema(description = PAGE_SIZE_DESCRIPTION)
    @Nullable
    Integer pageSize,

    @Schema(description = SORT_DESCRIPTION)
    @Nullable
    SortType sort,

    @Schema(description = IS_RELEASED_DESCRIPTION)
    @Nullable
    Boolean isReleased
) {
    public CursorPaginationInfoRequest(@Nullable Long lastProjectId, Long lastOrderCount,
                                       Integer pageSize, SortType sort,
                                       Boolean isReleased) {
        this.lastProjectId = lastProjectId;
        this.lastOrderCount = lastOrderCount;
        this.pageSize = (pageSize != null) ? pageSize : 24;
        this.sort = (sort != null) ? sort : SortType.createdAt;
        this.isReleased = (isReleased != null) ? isReleased : false;
    }

}
