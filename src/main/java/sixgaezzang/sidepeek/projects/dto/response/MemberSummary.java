package sixgaezzang.sidepeek.projects.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Builder
public record MemberSummary(
    Long id,
    String category,
    UserSummary userSummary
) {

}
