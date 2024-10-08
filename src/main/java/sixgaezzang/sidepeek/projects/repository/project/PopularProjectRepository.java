package sixgaezzang.sidepeek.projects.repository.project;

import java.time.LocalDate;
import java.util.List;
import sixgaezzang.sidepeek.projects.dto.response.ProjectBannerResponse;

public interface PopularProjectRepository {

    List<ProjectBannerResponse> findRankBetweenPeriod(LocalDate startDate, LocalDate endDate,
        int count);
}
