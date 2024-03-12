package sixgaezzang.sidepeek.projects.repository.project;

import java.util.List;
import sixgaezzang.sidepeek.projects.dto.request.CursorPaginationInfoRequest;
import sixgaezzang.sidepeek.projects.dto.response.CursorPaginationResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;

public interface ProjectRepositoryCustom {

    CursorPaginationResponse<ProjectListResponse> findByCondition(
        List<Long> likedProjectIds,
        CursorPaginationInfoRequest pageable);
}
