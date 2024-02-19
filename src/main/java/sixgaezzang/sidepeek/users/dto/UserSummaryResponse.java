package sixgaezzang.sidepeek.users.dto;

import lombok.Builder;
import sixgaezzang.sidepeek.users.domain.User;

@Builder
public record UserSummaryResponse(
    Long id,
    String nickname,
    String profileImageUrl
) {

    public static UserSummaryResponse from(User user) {
        return UserSummaryResponse.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .build();
    }

    // 비회원
    public static UserSummaryResponse from(String nickname) {
        return UserSummaryResponse.builder()
            .id(null)
            .nickname(nickname)
            .profileImageUrl(null)
            .build();
    }

}
