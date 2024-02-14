package sixgaezzang.sidepeek.users.dto;

import lombok.Builder;

@Builder
public record UserSummaryResponse(
    Long id,
    String nickname,
    String profileImageUrl
) {
}
