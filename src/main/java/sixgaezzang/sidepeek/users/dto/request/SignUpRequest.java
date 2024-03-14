package sixgaezzang.sidepeek.users.dto.request;

import static sixgaezzang.sidepeek.common.doc.description.UserDescription.NICKNAME_DESCRIPTION;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.EMAIL_FORMAT_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.EMAIL_IS_NULL;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PASSWORD_FORMAT_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.PASSWORD_IS_NULL;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import sixgaezzang.sidepeek.users.domain.Password;

@Schema(description = "회원가입 요청")
public record SignUpRequest(
    @Schema(description = "이메일, 이메일 형식 검사", example = "sidepeek@gmail.com")
    @NotBlank(message = EMAIL_IS_NULL)
    @Email(message = EMAIL_FORMAT_INVALID)
    String email,
    @Schema(description = "비밀번호, 비밀번호 형식 검사", example = "sidepeek123!")
    @NotBlank(message = PASSWORD_IS_NULL)
    @Pattern(regexp = Password.PASSWORD_REGXP, message = PASSWORD_FORMAT_INVALID)
    String password,
    @Schema(description = NICKNAME_DESCRIPTION, example = "jinny")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = MAX_NICKNAME_LENGTH, message = NICKNAME_OVER_MAX_LENGTH)
    String nickname
) {

}
