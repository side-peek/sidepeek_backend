package sixgaezzang.sidepeek.users.dto.request;

import static sixgaezzang.sidepeek.common.doc.description.CommonDescription.GITHUB_URL_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.UserDescription.BLOG_URL_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.UserDescription.CAREER_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.UserDescription.INTRODUCTION_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.UserDescription.JOB_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.UserDescription.NICKNAME_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.UserDescription.PROFILE_IMAGE_URL_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.UserDescription.USER_TECH_STACK_DESCRIPTION;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.GITHUB_URL_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.common.exception.message.TechStackErrorMessage.TECH_STACKS_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
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
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;

@Schema(description = "회원 프로필 수정 요청")
public record UpdateUserProfileRequest(
    @Schema(description = NICKNAME_DESCRIPTION, example = "의진")
    @NotNull(message = NICKNAME_IS_NULL)
    @Size(max = MAX_NICKNAME_LENGTH, message = NICKNAME_OVER_MAX_LENGTH)
    String nickname,

    @Schema(description = PROFILE_IMAGE_URL_DESCRIPTION, example = "https://sidepeek.image/profile1")
    @Size(max = MAX_TEXT_LENGTH, message = "")
    String profileImageUrl,

    @Schema(description = INTRODUCTION_DESCRIPTION, example = "전 최고의 백엔드 개발자입니다.")
    @Size(max = MAX_INTRODUCTION_LENGTH, message = INTRODUCTION_OVER_MAX_LENGTH)
    String introduction,

    @Schema(description = JOB_DESCRIPTION, example = "백엔드 개발자")
    @Size(max = MAX_JOB_LENGTH, message = JOB_OVER_MAX_LENGTH)
    String job,

    @Schema(description = CAREER_DESCRIPTION, example = "1-3년차")
    @Size(max = MAX_CAREER_LENGTH, message = CAREER_OVER_MAX_LENGTH)
    String career,

    @Schema(description = GITHUB_URL_DESCRIPTION, example = "https://github.com")
    @Size(max = MAX_TEXT_LENGTH, message = GITHUB_URL_OVER_MAX_LENGTH)
    String githubUrl,

    @Schema(description = BLOG_URL_DESCRIPTION, example = "https://medium.com")
    @Size(max = MAX_TEXT_LENGTH, message = BLOG_URL_OVER_MAX_LENGTH)
    String blogUrl,

    @Schema(description = USER_TECH_STACK_DESCRIPTION)
    @Size(max = MAX_TECH_STACK_COUNT, message = TECH_STACKS_OVER_MAX_COUNT)
    List<SaveTechStackRequest> techStacks
) {
}
