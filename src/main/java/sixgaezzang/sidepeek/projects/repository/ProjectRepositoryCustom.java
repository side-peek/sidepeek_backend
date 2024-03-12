package sixgaezzang.sidepeek.projects.repository;

import java.time.LocalDate;
import java.util.List;
import sixgaezzang.sidepeek.projects.dto.response.ProjectBannerResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;

public interface ProjectRepositoryCustom {

    List<ProjectListResponse> findAllBySortAndStatus(List<Long> likedProjectIds, String sort,
        boolean isReleased);

    List<ProjectBannerResponse> findAllPopularOfPeriod(LocalDate startDate, LocalDate endDate, int count);

}
