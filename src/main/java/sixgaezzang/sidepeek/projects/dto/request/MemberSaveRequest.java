package sixgaezzang.sidepeek.projects.dto.request;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_ID_NEGATIVE_OR_ZERO;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.NON_FELLOW_MEMBER_NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.ROLE_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "프로젝트 생성 요청에서 프로젝트 멤버 정보")
public record MemberSaveRequest(
    @Schema(description = "회원 멤버 유저 식별자(비회원 멤버이면 null)", example = "1")
    @Min(value = MIN_ID, message = MEMBER_ID_NEGATIVE_OR_ZERO)
    Long userId,

    @Schema(description = "비회원 멤버 닉네임(회원 멤버이면 null)", example = " ")
    @Size(max = MAX_NICKNAME_LENGTH, message = NON_FELLOW_MEMBER_NICKNAME_OVER_MAX_LENGTH)
    String nickname,

    @Schema(description = "멤버 역할", example = "백엔드")
    @Size(max = MAX_ROLE_LENGTH, message = ROLE_OVER_MAX_LENGTH)
    @NotBlank(message = "멤버 역할을 입력해주세요.")
    String role
) {

}
