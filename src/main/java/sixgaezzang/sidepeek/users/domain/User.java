package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.common.ValidationUtils.validateBlank;
import static sixgaezzang.sidepeek.common.ValidationUtils.validateEmail;
import static sixgaezzang.sidepeek.common.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.common.ValidationUtils.validatePassword;
import static sixgaezzang.sidepeek.common.ValidationUtils.validateURI;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import sixgaezzang.sidepeek.common.BaseTimeEntity;

@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    private static final int MAX_NICKNAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nickname", length = MAX_NICKNAME_LENGTH, nullable = false, unique = true)
    private String nickname;

    @Column(name = "provider", length = 50, nullable = false, columnDefinition = "VARCHAR(50)")
    private LoginType loginType;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "introduction", length = 100)
    private String introduction;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(name = "job", length = 30, columnDefinition = "VARCHAR(30)")
    @Enumerated(EnumType.STRING)
    private Job job;

    @Column(name = "career", length = 30, columnDefinition = "VARCHAR(30)")
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
        this.loginType = provider;
        this.email = email;
        this.password = password;
        this.githubUrl = githubUrl;
    }

    private void validateConstructorArguments(String nickname, LoginType provider, String email,
        String password, String githubUrl) {
        validateNickname(nickname);
        validateEmail(email, "이메일 형식이 올바르지 않습니다.");
        validateLoginCriteria(provider, password, githubUrl);
    }

    private void validateLoginCriteria(LoginType provider, String password, String githubUrl) {
        if (provider.isEmailType()) {
            validatePassword(password, "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상이여야 합니다.");
            validateBlank(githubUrl, "이메일 로그인 사용자는 회원가입 시 깃허브 링크를 설정할 수 없습니다.");
            return;
        }

        validateBlank(password, "소셜 로그인 사용자는 비밀번호를 입력할 수 없습니다.");

        if (provider.isGitHubType()) {
            validateURI(githubUrl, "GitHub URL 형식이 올바르지 않습니다.");
        }
    }

    private void validateNickname(String nickname) {
        validateNotBlank(nickname, "닉네임은 필수값입니다.");
        validateMaxLength(nickname, MAX_NICKNAME_LENGTH,
            "닉네임은 " + MAX_NICKNAME_LENGTH + "자 이하여야 합니다.");
    }

}
