package sixgaezzang.sidepeek.users.dto.response;

import static sixgaezzang.sidepeek.common.util.CommonConstant.BLANK_STRING;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.Builder;
import sixgaezzang.sidepeek.users.domain.User;

@Schema(description = "회원 상세 정보")
@Builder
public record UserSummary(
    @Schema(description = "회원 식별자(비회원은 null)", nullable = true, example = "1")
    Long id,

    @Schema(description = "소셜 로그인 회원 여부", nullable = true, example = "false")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean isSocialLogin,

    @Schema(description = "회원/비회원 닉네임", example = "의진")
    String nickname,

    @Schema(description = "회원 프로필 이미지, 비회원이거나 없으면 빈 문자열 반환", nullable = true,
        example = "https://user-images.githubusercontent.com/uijin.png")
    String profileImageUrl
) {

    public UserSummary(Long id, String nickname, String profileImageUrl) {
        this(id, null, nickname, Objects.isNull(profileImageUrl) ? BLANK_STRING : profileImageUrl);
    }

    public static UserSummary fromWithIsSocialLogin(User user, boolean isSocialLogin) {
        return UserSummary.builder()
            .id(user.getId())
            .isSocialLogin(isSocialLogin)
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .build();
    }

    public static UserSummary from(User user) {
        return UserSummary.builder()
            .id(user.getId())
            .isSocialLogin(null)
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .build();
    }

    // 멤버(회원)
    public static UserSummary from(User user, String nickname) {
        return UserSummary.builder()
            .id(user.getId())
            .isSocialLogin(null)
            .nickname(nickname)
            .profileImageUrl(user.getProfileImageUrl())
            .build();
    }

    // 멤버(비회원)
    public static UserSummary from(String nickname) {
        return UserSummary.builder()
            .id(null)
            .isSocialLogin(null)
            .nickname(nickname)
            .profileImageUrl(BLANK_STRING)
            .build();
    }

}
