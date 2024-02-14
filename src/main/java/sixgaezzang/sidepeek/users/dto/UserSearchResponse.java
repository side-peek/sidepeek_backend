package sixgaezzang.sidepeek.users.dto;

import java.util.List;

public record UserSearchResponse(
    List<UserSummaryResponse> users
) {
}
