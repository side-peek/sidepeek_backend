package sixgaezzang.sidepeek.users.dto.request;

import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CheckNicknameRequest(
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = MAX_NICKNAME_LENGTH, message = "닉네임은 " + MAX_NICKNAME_LENGTH + "자 이하여야 합니다.")
    String nickname
) {

}
