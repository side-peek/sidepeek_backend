package sixgaezzang.sidepeek.projects.repository.project;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sixgaezzang.sidepeek.projects.dto.request.FindProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.CursorPaginationResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.users.domain.User;

public interface ProjectRepositoryCustom {

    CursorPaginationResponse<ProjectListResponse> findByCondition(
        List<Long> likedProjectIds,
        FindProjectRequest request);

    Page<ProjectListResponse> findAllByUserJoined(List<Long> likedProjectIds, User user,
        Pageable pageable);

    Page<ProjectListResponse> findAllByUserLiked(List<Long> likedProjectIds, User user,
        Pageable pageable);

    Page<ProjectListResponse> findAllByUserCommented(List<Long> likedProjectIds, User user,
        Pageable pageable);
}
