package sixgaezzang.sidepeek.users.controller;

import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "User API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    @Parameters({
        @Parameter(name = "email", description = "이메일", example = "sidepeek6@gmail.com"),
        @Parameter(name = "password", description = "영문/숫자/특수문자 포함 8자 이상", example = "sidepeek6!"),
        @Parameter(name = "nickname", description = "닉네임", example = "육개짱"),
    })
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        Long id = userService.signUp(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/users/{id}")
            .buildAndExpand(id).toUri();

        return ResponseEntity.created(uri)
            .build();
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "비밀번호 수정")
    @ApiResponse(responseCode = "204", description = "비밀번호 수정 성공")
    public ResponseEntity<Void> updatePassword(
        @Schema(description = "로그인한 회원 식별자(토큰에서 추출)")
        @Login
        Long loginId,

        @Schema(description = "수정할 회원 식별자")
        @PathVariable
        Long id,

        @RequestBody
        @Valid
        UpdatePasswordRequest request
    ) {
        // TODO: 비밀번호 변경 서비스 기능 구현
        return ResponseEntity.noContent()
            .build();
    }

    @PostMapping("/email/check")
    @Operation(summary = "이메일 중복 확인")
    @ApiResponse(responseCode = "200", description = "이메일 중복 확인 성공")
    @Parameter(name = "email", description = "이메일", example = "sidepeek6@gmail.com")
    public ResponseEntity<CheckDuplicateResponse> checkEmailDuplicate(
        @RequestBody @Valid CheckEmailRequest request
    ) {
        CheckDuplicateResponse response = userService.checkEmailDuplicate(request.email());

        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping("/nickname/check")
    @Operation(summary = "닉네임 중복 확인")
    @ApiResponse(responseCode = "200", description = "닉네임 중복 확인 성공")
    @Parameter(name = "nickname", description = "닉네임", example = "육개짱")
    public ResponseEntity<CheckDuplicateResponse> checkNicknameDuplicate(
        @RequestBody @Valid CheckNicknameRequest request
    ) {
        CheckDuplicateResponse response = userService.checkNicknameDuplicate(request.nickname());

        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/nickname")
    @Operation(summary = "회원 닉네임 검색")
    @ApiResponse(responseCode = "200", description = "회원 검색 성공")
    @Parameter(name = "keyword", description = "검색어", example = "sixgaezzang6")
    public ResponseEntity<UserSearchResponse> searchByNickname(
        @RequestParam(required = false)
        @Size(max = MAX_NICKNAME_LENGTH, message = NICKNAME_OVER_MAX_LENGTH)
        String keyword
    ) {
        return ResponseEntity.ok()
            .body(userService.searchByNickname(keyword));
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 프로필 정보 조회")
    @ApiResponse(responseCode = "200", description = "회원 프로필 정보 조회 성공")
    public ResponseEntity<UserProfileResponse> getById(
        @Schema(description = "회원 식별자")
        @PathVariable
        Long id
    ) {
        return ResponseEntity.ok(userService.getProfileById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "회원 프로필 정보 수정")
    @ApiResponse(responseCode = "200", description = "회원 프로필 정보 조회 성공")
    public ResponseEntity<UserProfileResponse> update(
        @Schema(description = "로그인한 회원 식별자(토큰에서 추출)")
        @Login
        Long loginId,

        @Schema(description = "수정할 회원 식별자")
        @PathVariable
        Long id,

        @RequestBody
        @Valid
        UpdateUserProfileRequest request
    ) {
        // TODO: 프로필 정보 수정 서비스 로직 구현
        return null;
    }

}
