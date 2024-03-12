package sixgaezzang.sidepeek.users.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import sixgaezzang.sidepeek.users.domain.User;

@Schema(description = "회원 상세 정보")
@Builder
public record UserSummary(
    @Schema(description = "회원 식별자(비회원은 null)", example = "1")
    Long id,

    @Schema(description = "소셜 로그인 회원 여부", example = "false")
    boolean isSocialLogin,

    @Schema(description = "회원/비회원 닉네임", example = "의진")
    String nickname,

    @Schema(description = "회원 프로필 이미지(비회원은 null)", example = "https://user-images.githubusercontent.com/uijin.png")
    String profileImageUrl
) {

    public static UserSummary from(User user, boolean isSocialLogin) {
        return UserSummary.builder()
            .id(user.getId())
            .isSocialLogin(isSocialLogin)
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .build();
    }

    // 멤버(회원)
    public static UserSummary from(User user, String nickname, boolean isSocialLogin) {
        return UserSummary.builder()
            .id(user.getId())
            .isSocialLogin(isSocialLogin)
            .nickname(nickname)
            .profileImageUrl(user.getProfileImageUrl())
            .build();
    }

    // 멤버(비회원)
    public static UserSummary from(String nickname) {
        return UserSummary.builder()
            .id(null)
            .isSocialLogin(false)
            .nickname(nickname)
            .profileImageUrl(null)
            .build();
    }

}
