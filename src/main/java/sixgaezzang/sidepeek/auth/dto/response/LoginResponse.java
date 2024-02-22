package sixgaezzang.sidepeek.auth.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.users.dto.response.UserSummaryResponse;

@Builder
public record LoginResponse(
    String accessToken,
    UserSummaryResponse user
) {

}
