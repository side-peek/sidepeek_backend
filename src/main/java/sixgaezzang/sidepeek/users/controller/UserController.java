package sixgaezzang.sidepeek.users.controller;

import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import sixgaezzang.sidepeek.users.dto.request.CheckEmailRequest;
import sixgaezzang.sidepeek.users.dto.request.SignUpRequest;
import sixgaezzang.sidepeek.users.dto.response.CheckDuplicateResponse;
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
        Long id = userService.signUp(request, Provider.BASIC);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/users/{id}")
            .buildAndExpand(id).toUri();

        return ResponseEntity.created(uri)
            .build();
    }

    @GetMapping
    @Operation(summary = "회원 검색")
    @ApiResponse(responseCode = "200", description = "회원 검색 성공")
    @Parameter(name = "keyword", description = "검색어", example = "sixgaezzang6")
    public ResponseEntity<UserSearchResponse> searchByNickname(
        @RequestParam(required = false)
        @Size(max = MAX_NICKNAME_LENGTH, message = "최대 " + MAX_NICKNAME_LENGTH
            + "자의 키워드로 검색할 수 있습니다.")
        String keyword
    ) {
        return ResponseEntity.ok()
            .body(userService.searchByNickname(keyword));
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

}
