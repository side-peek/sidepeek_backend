package sixgaezzang.sidepeek.users.dto.request;

import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import sixgaezzang.sidepeek.users.domain.Password;

@Schema(description = "회원가입 요청")
public record SignUpRequest(
    @Schema(description = "이메일", example = "sidepeek@gmail.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,
    @Schema(description = "비밀번호", example = "sidepeek123!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = Password.PASSWORD_REGXP, message = "비밀번호는 8자 이상이며 영문, 숫자, 특수문자를 포함해야 합니다.")
    String password,
    @Schema(description = "닉네임", example = "jinny")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = MAX_NICKNAME_LENGTH, message = NICKNAME_OVER_MAX_LENGTH)
    String nickname
) {

}
