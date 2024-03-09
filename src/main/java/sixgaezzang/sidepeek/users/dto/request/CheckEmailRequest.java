package sixgaezzang.sidepeek.users.dto.request;

import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.EMAIL_FORMAT_INVALID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.EMAIL_IS_NULL;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "이메일 중복 확인 요청")
public record CheckEmailRequest(
    @Schema(description = "이메일", example = "sidepeek@gmail.com")
    @NotBlank(message = EMAIL_IS_NULL)
    @Email(message = EMAIL_FORMAT_INVALID)
    String email
) {

}
