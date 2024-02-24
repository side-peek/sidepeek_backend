package sixgaezzang.sidepeek.users.dto.response;

import java.util.List;
import sixgaezzang.sidepeek.users.domain.User;

public record UserSearchResponse(
    List<UserSummary> users
) {

    public static UserSearchResponse from(List<User> users) {
        List<UserSummary> userSummaries = users.stream()
            .map(UserSummary::from)
            .toList();

        return new UserSearchResponse(userSummaries);
    }

}
