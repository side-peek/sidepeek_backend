package sixgaezzang.sidepeek.users.dto.request;

import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "닉네임 중복 확인 요청")
public record CheckNicknameRequest(
    @Schema(description = "닉네임", example = "jinny")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = MAX_NICKNAME_LENGTH, message = NICKNAME_OVER_MAX_LENGTH)
    String nickname
) {

}
