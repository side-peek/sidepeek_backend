package sixgaezzang.sidepeek.users.dto.request;

import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NEW_PASSWORD_IS_NULL;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PASSWORD_FORMAT_INVALID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import sixgaezzang.sidepeek.users.domain.Password;

@Schema(description = "비밀번호 수정 요청")
public record UpdatePasswordRequest(
    @Schema(description = "기존 비밀번호, 기존 비밀번호와 일치 확인, 비밀번호 형식 검사", example = "sidepeek6!")
    @NotBlank(message = PASSWORD_FORMAT_INVALID)
    @Pattern(regexp = Password.PASSWORD_REGXP, message = PASSWORD_FORMAT_INVALID)
    String originalPassword,

    @Schema(description = "새로운 비밀번호, 비밀번호 형식 검사", example = "sidepeek678!")
    @NotBlank(message = NEW_PASSWORD_IS_NULL)
    @Pattern(regexp = Password.PASSWORD_REGXP, message = PASSWORD_FORMAT_INVALID)
    String password
) {

}
