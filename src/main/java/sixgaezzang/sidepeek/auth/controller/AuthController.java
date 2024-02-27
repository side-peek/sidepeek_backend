package sixgaezzang.sidepeek.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sixgaezzang.sidepeek.auth.dto.request.LoginRequest;
import sixgaezzang.sidepeek.auth.dto.request.ReissueTokenRequest;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;
import sixgaezzang.sidepeek.auth.service.AuthService;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @Parameters({
        @Parameter(name = "email", description = "이메일", example = "sidepeek6@gmail.com"),
        @Parameter(name = "password", description = "비밀번호", example = "sidepeek6!")
    })
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/me")
    @Operation(summary = "Access Token 검증")
    @ApiResponse(responseCode = "200", description = "Access Token 검증 성공")
    public ResponseEntity<UserSummary> validateToken(@Login Long loginId) {
        UserSummary response = authService.loadUser(loginId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue")
    @Operation(summary = "Access / Refresh Token 재발급")
    @ApiResponse(responseCode = "200", description = "Access / Refresh Token 재발급 성공")
    public ResponseEntity<LoginResponse> reissue(@RequestBody @Valid ReissueTokenRequest request) {
        LoginResponse response = authService.reissue(request);

        return ResponseEntity.ok(response);
    }
}
