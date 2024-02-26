package sixgaezzang.sidepeek.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReissueTokenRequest(
    @NotBlank(message = "Refresh Token은 필수값입니다.")
    String refreshToken
) {

}
