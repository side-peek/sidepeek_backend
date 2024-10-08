package sixgaezzang.sidepeek.projects.repository.project;

import java.time.LocalDate;
import java.util.List;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSummary;

public interface PopularProjectRepository {

    List<ProjectSummary> findRankBetweenPeriod(LocalDate startDate, LocalDate endDate,
        int count);
}
