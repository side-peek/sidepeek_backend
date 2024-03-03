package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateEmail;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.password.PasswordEncoder;
import sixgaezzang.sidepeek.common.domain.BaseTimeEntity;

@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    public static final int MAX_NICKNAME_LENGTH = 20;
    public static final int MAX_EMAIL_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nickname", length = MAX_NICKNAME_LENGTH, unique = true)
    private String nickname;

    @Column(name = "email", length = MAX_EMAIL_LENGTH, unique = true)
    private String email;

    @Embedded
    private Password password;

    @Column(name = "introduction", length = 100)
    private String introduction;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(name = "job", length = 30, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    private Job job;

    @Column(name = "career", length = 30, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    private Career career;

    @Column(name = "github_url", columnDefinition = "TEXT")
    private String githubUrl;

    @Column(name = "blog_url", columnDefinition = "TEXT")
    private String blogUrl;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    public User(String nickname, String email, Password password) {
        validateConstructorArguments(nickname, email);

        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public boolean checkPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return password != null && password.check(rawPassword, passwordEncoder);
    }

    private void validateConstructorArguments(String nickname, String email) {
        if (nickname != null) {
            validateNickname(nickname);
        }

        if (email != null) {
            validateEmail(email, "이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validateNickname(String nickname) {
        validateMaxLength(nickname, MAX_NICKNAME_LENGTH,
            "닉네임은 " + MAX_NICKNAME_LENGTH + "자 이하여야 합니다.");
    }
}
