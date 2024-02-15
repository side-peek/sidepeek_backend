package sixgaezzang.sidepeek.users.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sixgaezzang.sidepeek.users.domain.Provider;
import sixgaezzang.sidepeek.users.dto.SignUpRequest;
import sixgaezzang.sidepeek.users.dto.UserSearchResponse;
import sixgaezzang.sidepeek.users.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        Long id = userService.signUp(request, Provider.BASIC);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/users/{id}")
            .buildAndExpand(id).toUri();

        return ResponseEntity.created(uri)
            .build();
    }

    @GetMapping
    public ResponseEntity<UserSearchResponse> searchByNickname(
        @RequestParam(required = false)
        @Size(max = 20, message = "최대 20자의 키워드로 검색할 수 있습니다.")
        String keyword
    ) {
        return ResponseEntity.ok()
            .body(userService.searchByNickname(keyword));
    }

}
