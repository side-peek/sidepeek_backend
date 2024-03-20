package sixgaezzang.sidepeek.auth.oauth.service.strategy;

import static java.util.Objects.isNull;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.service.UserService;

@RequiredArgsConstructor
public class GithubUserInfoMapper implements UserInfoMapper {

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_EMAIL = "email";
    private static final String ATTRIBUTE_NICKNAME = "login";
    private static final String ATTRIBUTE_GITHUB_URL = "html_url";
    private static final String ATTRIBUTE_BLOG_URL = "blog";
    private static final String ATTRIBUTE_PROFILE_IMAGE_URL = "avatar_url";

    private final UserService userService;

    @Override
    public User mapToUser(Map<String, Object> attributes) {
        return User.builder()
            .email(getEmail(attributes))
            .nickname(getNickname(attributes))
            .profileImageUrl(getAttribute(attributes, ATTRIBUTE_PROFILE_IMAGE_URL))
            .githubUrl(getAttribute(attributes, ATTRIBUTE_GITHUB_URL))
            .blogUrl(getAttribute(attributes, ATTRIBUTE_BLOG_URL))
            .build();
    }

    @Override
    public String getProviderId(Map<String, Object> attributes) {
        return getAttribute(attributes, ATTRIBUTE_ID);
    }

    private String getAttribute(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        return value != null ? value.toString() : null;
    }

    private String getEmail(Map<String, Object> attributes) {
        String email = getAttribute(attributes, ATTRIBUTE_EMAIL);

        if (isNull(email) || userService.checkEmailDuplicate(email).isDuplicated()) {
            return null;
        }

        return email;
    }

    private String getNickname(Map<String, Object> attributes) {
        String nickname = getAttribute(attributes, ATTRIBUTE_NICKNAME);

        if (isNull(nickname) || userService.checkNicknameDuplicate(nickname).isDuplicated()) {
            return null;
        }

        return nickname;
    }
}
