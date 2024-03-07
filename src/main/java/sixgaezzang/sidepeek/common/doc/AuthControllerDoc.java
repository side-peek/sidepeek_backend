package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.auth.dto.request.LoginRequest;
import sixgaezzang.sidepeek.auth.dto.request.ReissueTokenRequest;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@Tag(name = "Auth", description = "회원 인증 API")
public interface AuthControllerDoc {

    @Operation(summary = "이메일/비밀번호 로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @Parameters({
        @Parameter(name = "email", description = "이메일", example = "sidepeek6@gmail.com"),
        @Parameter(name = "password", description = "비밀번호", example = "sidepeek6!")
    })
    ResponseEntity<LoginResponse> login(LoginRequest request);

    @Operation(summary = "Access Token 검증", description = "Access Token을 통해 현재 로그인 된 사용자의 정보를 응답받습니다.")
    @ApiResponse(responseCode = "200", description = "Access Token 검증 성공")
    ResponseEntity<UserSummary> validateToken(Long loginId);

    @Operation(summary = "Access / Refresh Token 재발급")
    @ApiResponse(responseCode = "200", description = "Access / Refresh Token 재발급 성공")
    ResponseEntity<LoginResponse> reissue(ReissueTokenRequest request);
}
