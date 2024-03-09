package sixgaezzang.sidepeek.users.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import sixgaezzang.sidepeek.users.domain.User;

@Schema(description = "회원 검색 응답")
public record UserSearchResponse(
    @Schema(description = "회원 목록")
    List<UserSummary> users
) {

    public static UserSearchResponse from(List<User> users) {
        List<UserSummary> userSummaries = users.stream()
            .map(UserSummary::from)
            .toList();

        return new UserSearchResponse(userSummaries);
    }

}
