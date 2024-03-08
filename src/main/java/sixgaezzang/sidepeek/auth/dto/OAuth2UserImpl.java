package sixgaezzang.sidepeek.auth.dto;

import java.util.Collection;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import sixgaezzang.sidepeek.auth.domain.AuthProvider;
import sixgaezzang.sidepeek.users.domain.User;

@Getter
public class OAuth2UserImpl extends DefaultOAuth2User {

    private User user;
    private AuthProvider authProvider;

    @Builder
    public OAuth2UserImpl(Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes, String nameAttributeKey, User user,
        AuthProvider authProvider) {
        super(authorities, attributes, nameAttributeKey);
        this.user = user;
        this.authProvider = authProvider;
    }
}
