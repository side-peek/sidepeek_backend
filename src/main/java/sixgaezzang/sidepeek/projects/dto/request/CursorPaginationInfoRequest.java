package sixgaezzang.sidepeek.projects.dto.request;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record CursorPaginationInfoRequest(

    @Nullable
    Long lastProjectId,

    @Nullable
    Long lastOrderCount,

    @Nullable
    Integer pageSize,

    @Nullable
    SortType sort,

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
