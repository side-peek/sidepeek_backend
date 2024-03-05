package sixgaezzang.sidepeek.users.dto.request;

import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.BLOG_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.CAREER_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.INTRODUCTION_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.JOB_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_IS_NULL;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_CAREER_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_INTRODUCTION_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_JOB_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "회원 프로필 수정 요청 정보")
public record UpdateUserProfileRequest(
    @Schema(description = "회원 닉네임", example = "의진")
    @NotNull(message = NICKNAME_IS_NULL)
    @Size(max = MAX_NICKNAME_LENGTH, message = NICKNAME_OVER_MAX_LENGTH)
    String nickname,

    @Schema(description = "회원 프로필 이미지 URL", example = "https://sidepeek.image/profile1")
    @Size(max = MAX_TEXT_LENGTH, message = "")
    String profileImageUrl,

    @Schema(description = "회원 소개", example = "전 최고의 백엔드 개발자입니다.")
    @Size(max = MAX_INTRODUCTION_LENGTH, message = INTRODUCTION_OVER_MAX_LENGTH)
    String introduction,

    @Schema(description = "회원 직업", example = "백엔드 개발자")
    @Size(max = MAX_JOB_LENGTH, message = JOB_OVER_MAX_LENGTH)
    String job,

    @Schema(description = "회원 경력", example = "1-3년차")
    @Size(max = MAX_CAREER_LENGTH, message = CAREER_OVER_MAX_LENGTH)
    String career,

    @Schema(description = "회원 깃허브 URL", example = "https://github.com")
    @Size(max = MAX_TEXT_LENGTH, message = GITHUB_URL_OVER_MAX_LENGTH)
    String githubUrl,

    @Schema(description = "회원 블로그 URL", example = "https://medium.com")
    @Size(max = MAX_TEXT_LENGTH, message = BLOG_URL_OVER_MAX_LENGTH)
    String blogUrl,

    @Schema(description = "회원 기술 스택 목록")
    List<UserSkillRequest> techStacks
) {
}
