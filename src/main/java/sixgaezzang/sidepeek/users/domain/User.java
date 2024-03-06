package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateEmail;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.BLANK_NICKNAME;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.EXCESSIVE_NICKNAME_LENGTH;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.INVALID_EMAIL_FORMAT;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_ALREADY_DELETED;
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
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.password.PasswordEncoder;
import sixgaezzang.sidepeek.common.domain.BaseTimeEntity;
import sixgaezzang.sidepeek.common.util.ValidationUtils;
import sixgaezzang.sidepeek.users.dto.request.UpdateUserProfileRequest;
import sixgaezzang.sidepeek.users.util.validation.UserValidator;

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
        return password.check(rawPassword, passwordEncoder);
    }

    public void update(UpdateUserProfileRequest request) {
        // Required
        setNickname(request.nickname());

        // Option
        setIntroduction(request.introduction());
        setProfileImageUrl(request.profileImageUrl());
        setJob(request.job());
        setCareer(request.career());
        setGithubUrl(request.githubUrl());
        setBlogUrl(request.blogUrl());
    }

    public void softDelete() {
        if (Objects.isNull(this.deletedAt)) {
            this.deletedAt = LocalDateTime.now();
            return;
        }
        throw new IllegalStateException(USER_ALREADY_DELETED);
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

    private void setNickname(String nickname) {
        if (!Objects.equals(this.nickname, nickname)) {
            UserValidator.validateNickname(nickname);
            this.nickname = nickname;
        }
    }

    private void setPassword(Password password) {
        if (!Objects.equals(this.password, password)) {
            UserValidator.validatePassword(password);
            this.password = password;
        }
    }

    private void setIntroduction(String introduction) {
        if (Objects.nonNull(introduction) && !Objects.equals(this.introduction, introduction)) {
            UserValidator.validateIntroduction(introduction);
            this.introduction = introduction;
        }
    }

    private void setProfileImageUrl(String profileImageUrl) {
        if (Objects.nonNull(profileImageUrl) && !Objects.equals(this.profileImageUrl, profileImageUrl)) {
            UserValidator.validateProfileImageUrl(profileImageUrl);
            this.profileImageUrl = profileImageUrl;
        }
    }

    private void setJob(String jobName) {
        if (Objects.nonNull(jobName) && !jobName.isBlank() && !Objects.equals(this.job.getName(), jobName)) {
            UserValidator.validateJob(jobName);
            this.job = Job.valueOf(jobName);
        }
    }

    private void setCareer(String careerDescription) {
        if (Objects.nonNull(careerDescription) && !Objects.equals(this.career.getDescription(), careerDescription)) {
            UserValidator.validateCareer(careerDescription);
            this.career = Career.valueOf(careerDescription);
        }
    }

    private void setGithubUrl(String githubUrl) {
        if (Objects.nonNull(githubUrl) && !Objects.equals(this.githubUrl, githubUrl)) {
            ValidationUtils.validateGithubUrl(githubUrl);
            this.githubUrl = githubUrl;
        }
    }

    private void setBlogUrl(String blogUrl) {
        if (Objects.nonNull(blogUrl) && !Objects.equals(this.blogUrl, blogUrl)) {
            UserValidator.validateBlogUrl(blogUrl);
            this.blogUrl = blogUrl;
        }
    }

}
