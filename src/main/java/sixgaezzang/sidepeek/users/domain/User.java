package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateEmail;
import static sixgaezzang.sidepeek.users.exception.UserErrorCode.INVALID_EMAIL_FORMAT;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_ALREADY_DELETED;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateBlogUrl;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateCareer;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateIntroduction;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateJob;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateNickname;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateProfileImageUrl;

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

    public void softDelete() { // TODO: 회원탈퇴할 때 언젠가는 쓰일 것 같아서 구현
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

    private void setNickname(String nickname) {
        if (!Objects.equals(this.nickname, nickname)) {
            validateNickname(nickname);
            this.nickname = nickname;
        }
    }

    private void setIntroduction(String introduction) {
        if (Objects.nonNull(introduction) && !Objects.equals(this.introduction, introduction)) {
            validateIntroduction(introduction);
            this.introduction = introduction;
        }
    }

    private void setProfileImageUrl(String profileImageUrl) {
        if (Objects.nonNull(profileImageUrl) && !Objects.equals(this.profileImageUrl, profileImageUrl)) {
            validateProfileImageUrl(profileImageUrl);
            this.profileImageUrl = profileImageUrl;
        }
    }

    private void setJob(String jobName) {
        if (Objects.nonNull(jobName) && !jobName.isBlank()) {
            validateJob(jobName);
            if (!Objects.equals(this.job, Job.valueOf(jobName))) {
                this.job = Job.valueOf(jobName);
            }
        }
    }

    private void setCareer(String careerDescription) {
        if (Objects.nonNull(careerDescription)) {
            validateCareer(careerDescription);
            if (!Objects.equals(this.career, Career.valueOf(careerDescription))) {
                this.career = Career.valueOf(careerDescription);
            }
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
            validateBlogUrl(blogUrl);
            this.blogUrl = blogUrl;
        }
    }

}
