package sixgaezzang.sidepeek.users.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import sixgaezzang.sidepeek.users.domain.Career;
import sixgaezzang.sidepeek.users.domain.Job;
import sixgaezzang.sidepeek.users.domain.User;

@Schema(description = "회원 프로필 정보")
@Builder
public record UserProfileResponse(
    @Schema(description = "소셜 로그인 회원 여부", example = "false")
    boolean isSocialLogin,

    @Schema(description = "회원/비회원 닉네임", example = "의진")
    String nickname,

    @Schema(description = "회원 프로필 이미지(비회원은 null)", example = "https://user-images.githubusercontent.com/uijin.png")
    String profileImageUrl,

    @Schema(description = "회원 소개", example = "전 최고의 백엔드 개발자입니다.")
    String introduction,

    @Schema(description = "회원 직업", example = "백엔드 개발자")
    String job,

    @Schema(description = "회원 경력", example = "1-3년차")
    String career,

    @Schema(description = "회원 깃허브 URL", example = "https://github.com")
    String githubUrl,

    @Schema(description = "회원 블로그 URL", example = "https://medium.com")
    String blogUrl,

    @Schema(description = "회원 기술 스택 목록")
    List<UserSkillSummary> techStacks
) {
    public static UserProfileResponse from(User user, List<UserSkillSummary> techStacks) {
        Job userJob = user.getJob();
        String jobName = Objects.nonNull(userJob) ? userJob.getName() : null;

        Career userCareer = user.getCareer();
        String careerDescription = Objects.nonNull(userCareer) ? userCareer.getDescription() : null;

        return UserProfileResponse.builder()
//            .isSocialLogin()
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .introduction(user.getIntroduction())
            .job(jobName)
            .career(careerDescription)
            .githubUrl(user.getGithubUrl())
            .blogUrl(user.getBlogUrl())
            .techStacks(techStacks)
            .build();
    }
}
