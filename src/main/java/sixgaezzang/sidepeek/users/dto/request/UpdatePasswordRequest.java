package sixgaezzang.sidepeek.users.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import sixgaezzang.sidepeek.users.domain.Password;

@Schema(description = "비밀번호 수정 요청 정보")
public record UpdatePasswordRequest(
    @Schema(description = "기존 비밀번호", example = "sidepeek6!")
    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    @Pattern(regexp = Password.PASSWORD_REGXP, message = "비밀번호는 8자 이상이며 영문, 숫자, 특수문자를 포함해야 합니다.")
    String originalPassword,

    @Schema(description = "새로운 비밀번호", example = "sidepeek678!")
    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    @Pattern(regexp = Password.PASSWORD_REGXP, message = "비밀번호는 8자 이상이며 영문, 숫자, 특수문자를 포함해야 합니다.")
    String password
) {
}
