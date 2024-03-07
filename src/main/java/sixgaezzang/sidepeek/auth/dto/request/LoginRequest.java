package sixgaezzang.sidepeek.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 요청")
public record LoginRequest(
    @Schema(description = "이메일", example = "sidepeek@gmail.com")
    @NotBlank(message = "이메일을 입력해 주세요.")
    String email,
    @Schema(description = "비밀번호", example = "sidepeek6!")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    String password
) {

}
