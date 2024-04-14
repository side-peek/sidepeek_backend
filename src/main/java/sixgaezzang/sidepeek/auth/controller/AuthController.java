package sixgaezzang.sidepeek.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sixgaezzang.sidepeek.auth.dto.request.LoginRequest;
import sixgaezzang.sidepeek.auth.dto.request.ReissueTokenRequest;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;
import sixgaezzang.sidepeek.auth.service.AuthService;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.common.doc.AuthControllerDoc;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDoc {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/login/{provider}")
    public ResponseEntity<LoginResponse> socialLogin(@PathVariable String provider,
        @RequestParam String code) {
        LoginResponse response = authService.socialLogin(provider, code);

        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/me")
    public ResponseEntity<UserSummary> validateToken(@Login Long loginId) {
        UserSummary response = authService.loadUser(loginId);

        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(@RequestBody @Valid ReissueTokenRequest request) {
        LoginResponse response = authService.reissue(request);

        return ResponseEntity.ok(response);
    }
}
