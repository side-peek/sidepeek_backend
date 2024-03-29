package sixgaezzang.sidepeek.projects.dto.request;

import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.MEMBER_NICKNAME_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.MEMBER_ROLE_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ProjectDescription.MEMBER_USER_ID_DESCRIPTION;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_ID_NEGATIVE_OR_ZERO;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_IS_NULL;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.member.Member;
import sixgaezzang.sidepeek.users.domain.User;

@Schema(description = "프로젝트 생성 요청에서 프로젝트 멤버 정보")
public record SaveMemberRequest(
    @Schema(description = MEMBER_USER_ID_DESCRIPTION, example = "1")
    @Min(value = MIN_ID, message = MEMBER_ID_NEGATIVE_OR_ZERO)
    Long id,

    @Schema(description = MEMBER_NICKNAME_DESCRIPTION, example = "의진")
    @Size(max = MAX_NICKNAME_LENGTH, message = NICKNAME_OVER_MAX_LENGTH)
    @NotBlank(message = NICKNAME_IS_NULL)
    String nickname,

    @Schema(description = MEMBER_ROLE_DESCRIPTION, example = "백엔드")
    @Size(max = MAX_ROLE_LENGTH, message = ROLE_OVER_MAX_LENGTH)
    @NotBlank(message = ROLE_IS_NULL)
    String role
) {
    public Member toEntity(Project project, User user) {
        return Member.builder()
            .project(project)
            .user(user)
            .nickname(this.nickname())
            .role(this.role())
            .build();
    }
}
