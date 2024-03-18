package sixgaezzang.sidepeek.auth.oauth.service.strategy;

import java.util.Map;
import sixgaezzang.sidepeek.users.domain.User;

public class GithubUserInfoMapper implements UserInfoMapper {

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_EMAIL = "email";
    private static final String ATTRIBUTE_NICKNAME = "login";
    private static final String ATTRIBUTE_GITHUB_URL = "html_url";
    private static final String ATTRIBUTE_BLOG_URL = "blog";
    private static final String ATTRIBUTE_PROFILE_IMAGE_URL = "avatar_url";

    @Override
    public User mapToUser(Map<String, Object> attributes) {
        User user = User.builder()
            .email(getAttribute(attributes, ATTRIBUTE_EMAIL))
            .nickname(getAttribute(attributes, ATTRIBUTE_NICKNAME))
            .profileImageUrl(getAttribute(attributes, ATTRIBUTE_PROFILE_IMAGE_URL))
            .githubUrl(getAttribute(attributes, ATTRIBUTE_GITHUB_URL))
            .blogUrl(getAttribute(attributes, ATTRIBUTE_BLOG_URL))
            .build();

        return user;
    }

    @Override
    public String getProviderId(Map<String, Object> attributes) {
        return getAttribute(attributes, ATTRIBUTE_ID);
    }

    private String getAttribute(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        return value != null ? value.toString() : null;
    }
}
