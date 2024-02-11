package sixgaezzang.sidepeek.users.domain;

import static io.micrometer.common.util.StringUtils.isBlank;
import static io.micrometer.common.util.StringUtils.isNotBlank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import sixgaezzang.sidepeek.common.BaseTimeEntity;

@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NOT NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+[.][0-9A-Za-z]+$");

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&^]).{8,}$");

    private static final Pattern URI_PATTERN = Pattern.compile(
        "^(https?)://([^:/\\s]+)(:([^/]*))?((/[^\\s/]+)*)?/?([^#\\s?]*)(\\?([^#\\s]*))?(#(\\w*))?$");

    private static final int MAX_NICKNAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nickname", length = MAX_NICKNAME_LENGTH, nullable = false, unique = true)
    private String nickname;

    @Column(name = "provider", length = 50, nullable = false)
    private LoginType loginType;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "introduction", length = 100)
    private String introduction;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(name = "job", length = 30)
    @Enumerated(EnumType.STRING)
    private Job job;

    @Column(name = "career", length = 30)
    @Enumerated(EnumType.STRING)
    private Career career;

    @Column(name = "github_url", columnDefinition = "TEXT")
    private String githubUrl;

    @Column(name = "blog_url", columnDefinition = "TEXT")
    private String blogUrl;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    public User(String nickname, LoginType provider, String email, String password,
        String githubUrl) {
        validateConstructorArguments(nickname, provider, email, password, githubUrl);

        this.nickname = nickname;
        this.provider = provider;
        this.email = email;
        this.password = password;
        this.githubUrl = githubUrl;
    }

    private void validateConstructorArguments(String nickname, LoginType provider, String email,
        String password, String githubUrl) {
        validateNickname(nickname);
        validateRegex(email, EMAIL_PATTERN, "이메일 형식이 올바르지 않습니다.");
        validateLoginCriteria(provider, password, githubUrl);
    }

    private void validateLoginCriteria(LoginType provider, String password, String githubUrl) {
        if (provider.isBasicType()) {
            validateRegex(password, PASSWORD_PATTERN,
                "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상이여야 합니다.");
        } else {
            validateBlank(password, "소셜 로그인 사용자는 비밀번호를 입력할 수 없습니다.");
        }

        if (provider.isGitHubType()) {
            validateRegex(githubUrl, URI_PATTERN, "GitHub URL 형식이 올바르지 않습니다.");
        }
    }

    private void validateNickname(String nickname) {
        validateNotBlank(nickname, "닉네임은 필수값입니다.");
        validateMax(nickname, MAX_NICKNAME_LENGTH, "닉네임은 " + MAX_NICKNAME_LENGTH + "자 이하여야 합니다.");
    }

    private void validateRegex(String input, Pattern pattern, String message) {
        if (!pattern.matcher(input).matches()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateMax(String input, int maxLength, String message) {
        if (input.length() > maxLength) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateNotBlank(String input, String message) {
        if (isBlank(input)) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateBlank(String input, String message) {
        if (isNotBlank(input)) {
            throw new IllegalArgumentException(message);
        }
    }

}
