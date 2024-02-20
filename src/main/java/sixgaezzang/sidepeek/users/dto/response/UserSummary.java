package sixgaezzang.sidepeek.users.dto.response;

import lombok.Builder;
import sixgaezzang.sidepeek.users.domain.User;

@Builder
public record UserSummary(
    Long id,
    String nickname,
    String profileImageUrl
) {

    public static UserSummary from(User user) {
        return UserSummary.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .build();
    }

}
