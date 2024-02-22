package sixgaezzang.sidepeek.auth.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Builder
public record LoginResponse(
    String accessToken,
    UserSummary user
) {

}
