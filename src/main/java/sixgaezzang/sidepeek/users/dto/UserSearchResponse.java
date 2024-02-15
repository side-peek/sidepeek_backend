package sixgaezzang.sidepeek.users.dto;

import java.util.List;
import sixgaezzang.sidepeek.users.domain.User;

public record UserSearchResponse(
    List<UserSummaryResponse> users
) {

    public static UserSearchResponse from(List<User> users) {
        List<UserSummaryResponse> userSummaryResponses = users.stream()
            .map(UserSummaryResponse::from)
            .toList();

        return new UserSearchResponse(userSummaryResponses);
    }

}
