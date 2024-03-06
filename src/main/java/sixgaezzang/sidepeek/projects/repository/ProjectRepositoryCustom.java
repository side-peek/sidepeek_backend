package sixgaezzang.sidepeek.projects.repository;

import java.util.List;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;

public interface ProjectRepositoryCustom {

    List<ProjectListResponse> findAllBySortAndStatus(List<Long> likedProjectIds, String sort,
        boolean isReleased);
}
