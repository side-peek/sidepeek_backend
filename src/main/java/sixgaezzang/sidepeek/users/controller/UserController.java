package sixgaezzang.sidepeek.users.controller;

import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.common.doc.UserControllerDoc;
import sixgaezzang.sidepeek.users.dto.request.CheckEmailRequest;
import sixgaezzang.sidepeek.users.dto.request.CheckNicknameRequest;
import sixgaezzang.sidepeek.users.dto.request.SignUpRequest;
import sixgaezzang.sidepeek.users.dto.request.UpdatePasswordRequest;
import sixgaezzang.sidepeek.users.dto.request.UpdateUserProfileRequest;
import sixgaezzang.sidepeek.users.dto.response.CheckDuplicateResponse;
import sixgaezzang.sidepeek.users.dto.response.UserProfileResponse;
import sixgaezzang.sidepeek.users.dto.response.UserSearchResponse;
import sixgaezzang.sidepeek.users.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {

    private final UserService userService;

    @Override
    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        Long id = userService.signUp(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/users/{id}")
            .buildAndExpand(id).toUri();

        return ResponseEntity.created(uri)
            .build();
    }

    @Override
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
        @Login Long loginId,
        @PathVariable Long id,
        @RequestBody @Valid UpdatePasswordRequest request
    ) {
        // TODO: 비밀번호 변경 서비스 기능 구현
        return ResponseEntity.noContent()
            .build();
    }

    @Override
    @PostMapping("/email/check")
    public ResponseEntity<CheckDuplicateResponse> checkEmailDuplicate(
        @RequestBody @Valid CheckEmailRequest request
    ) {
        CheckDuplicateResponse response = userService.checkEmailDuplicate(request.email());

        return ResponseEntity.ok()
            .body(response);
    }

    @Override
    @PostMapping("/nickname/check")
    public ResponseEntity<CheckDuplicateResponse> checkNicknameDuplicate(
        @RequestBody @Valid CheckNicknameRequest request
    ) {
        CheckDuplicateResponse response = userService.checkNicknameDuplicate(request.nickname());

        return ResponseEntity.ok()
            .body(response);
    }

    @Override
    @GetMapping("/nickname")
    public ResponseEntity<UserSearchResponse> searchByNickname(
        @RequestParam(required = false)
        @Size(max = MAX_NICKNAME_LENGTH, message = NICKNAME_OVER_MAX_LENGTH)
        String keyword
    ) {
        return ResponseEntity.ok()
            .body(userService.searchByNickname(keyword));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getById(@PathVariable Long id) {
        // TODO: 프로필 정보 조회 서비스 로직 구현
        return null;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> update(
        @Login Long loginId,
        @PathVariable Long id,
        @RequestBody @Valid UpdateUserProfileRequest request
    ) {
        // TODO: 프로필 정보 수정 서비스 로직 구현
        return null;
    }

}
