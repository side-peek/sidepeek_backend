package sixgaezzang.sidepeek.projects.dto.request;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MIN_ID;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberSaveRequest(
    @Min(value = MIN_ID, message = "멤버 회원 id는 " + MIN_ID + "보다 작을 수 없습니다.")
    Long userId,

    @Size(max = MAX_NICKNAME_LENGTH,
        message = "멤버 닉네임은 " + MAX_NICKNAME_LENGTH + "자를 넘을 수 없습니다.")
    String nickname,

    @Size(max = MAX_ROLE_LENGTH,
        message = "멤버 역할은 " + MAX_ROLE_LENGTH + "자를 넘을 수 없습니다.")
    @NotBlank(message = "멤버 역할을 입력해주세요.")
    String role
) {
}
