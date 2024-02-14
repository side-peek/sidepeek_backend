package sixgaezzang.sidepeek.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import sixgaezzang.sidepeek.users.domain.Password;

public record SignUpRequest(
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = Password.PASSWORD_REGXP, message = "비밀번호는 8자 이상이며 영문, 숫자, 특수문자를 포함해야 합니다.")
    String password,
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Length(max = 20, message = "닉네임은 20자 이하여야 합니다.")
    String nickname
) {

}
