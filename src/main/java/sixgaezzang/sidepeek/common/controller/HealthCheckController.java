package sixgaezzang.sidepeek.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sixgaezzang.sidepeek.auth.dto.response.LoginResponse;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<LoginResponse> healthCheck() {
        return ResponseEntity.ok()
            .build();
    }
}
