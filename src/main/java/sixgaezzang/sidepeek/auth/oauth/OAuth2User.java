package sixgaezzang.sidepeek.auth.oauth;

import lombok.Builder;
import lombok.Getter;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.users.domain.User;

@Getter
@Builder
public class OAuth2User {

    private User user;
    private AuthProvider authProvider;
}
