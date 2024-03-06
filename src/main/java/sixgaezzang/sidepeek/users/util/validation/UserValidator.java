package sixgaezzang.sidepeek.users.util.validation;

import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_IS_INVALID;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateNotBlank;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.BLOG_URL_IS_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.BLOG_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.CAREER_IS_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.INTRODUCTION_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.JOB_IS_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_IS_NULL;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PROFILE_IMAGE_URL_IS_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PROFILE_IMAGE_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_ID_IS_NULL;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_INTRODUCTION_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import sixgaezzang.sidepeek.common.util.ValidationUtils;
import sixgaezzang.sidepeek.users.domain.Career;
import sixgaezzang.sidepeek.users.domain.Job;
import sixgaezzang.sidepeek.users.domain.Password;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserValidator {

    public static void validateUserId(Long id) {
        Assert.notNull(id, USER_ID_IS_NULL);
    }

    public static void validateNickname(String nickname) {
        validateNotBlank(nickname, NICKNAME_IS_NULL);
        validateMaxLength(nickname, MAX_NICKNAME_LENGTH, NICKNAME_OVER_MAX_LENGTH);
    }

    public static void validateIntroduction(String introduction) {
        validateMaxLength(introduction, MAX_INTRODUCTION_LENGTH, INTRODUCTION_OVER_MAX_LENGTH);
    }

    public static void validateJob(String jobName) {
        Arrays.stream(Job.values())
            .filter(job -> jobName.equals(job.getName()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(JOB_IS_INVALID));
    }

    public static void validateCareer(String careerDescription) {
        Arrays.stream(Career.values())
            .filter(career -> careerDescription.equals(career.getDescription()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(CAREER_IS_INVALID));
    }

    public static void validateProfileImageUrl(String profileImageUrl) {
        validateMaxLength(profileImageUrl, MAX_TEXT_LENGTH, PROFILE_IMAGE_URL_OVER_MAX_LENGTH);
        ValidationUtils.validateURI(profileImageUrl, PROFILE_IMAGE_URL_IS_INVALID);
    }

    public static void validateGithubUrl(String githubUrl) {
        validateMaxLength(githubUrl, MAX_TEXT_LENGTH, GITHUB_URL_OVER_MAX_LENGTH);
        ValidationUtils.validateURI(githubUrl, GITHUB_URL_IS_INVALID);
    }

    public static void validateBlogUrl(String blogUrl) {
        validateMaxLength(blogUrl, MAX_TEXT_LENGTH, BLOG_URL_OVER_MAX_LENGTH);
        ValidationUtils.validateURI(blogUrl, BLOG_URL_IS_INVALID);
    }

    public static void validatePassword(Password password) {
        // TODO: 비밀번호 유효성 검사
    }

}
