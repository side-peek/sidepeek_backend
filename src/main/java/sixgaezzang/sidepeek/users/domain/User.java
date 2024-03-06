package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateEmail;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.BLANK_NICKNAME;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.EXCESSIVE_NICKNAME_LENGTH;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.INVALID_EMAIL_FORMAT;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

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
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.password.PasswordEncoder;
import sixgaezzang.sidepeek.common.domain.BaseTimeEntity;

@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nickname", length = MAX_NICKNAME_LENGTH, nullable = false, unique = true)
    private String nickname;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Embedded
    @Setter
    private Password password;

    @Column(name = "introduction", length = 100)
    @Setter
    private String introduction;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    @Setter
    private String profileImageUrl;

    @Column(name = "job", length = 30, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    @Setter
    private Job job;

    @Column(name = "career", length = 30, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    @Setter
    private Career career;

    @Column(name = "github_url", columnDefinition = "TEXT")
    @Setter
    private String githubUrl;

    @Column(name = "blog_url", columnDefinition = "TEXT")
    @Setter
    private String blogUrl;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    @Setter
    private LocalDateTime deletedAt;

    @Builder
    public User(String nickname, String email, Password password) {
        validateConstructorArguments(nickname, email);

        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public boolean checkPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return password.check(rawPassword, passwordEncoder);
    }

    private void validateConstructorArguments(String nickname, String email) {
        validateNickname(nickname);
        validateEmail(email, INVALID_EMAIL_FORMAT.getMessage());
    }

    private void validateNickname(String nickname) {
        validateNotBlank(nickname, BLANK_NICKNAME.getMessage());
        validateMaxLength(nickname, MAX_NICKNAME_LENGTH,
            EXCESSIVE_NICKNAME_LENGTH.getMessage());
    }
}
