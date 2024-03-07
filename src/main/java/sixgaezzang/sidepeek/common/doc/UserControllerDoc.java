package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;
import sixgaezzang.sidepeek.users.dto.request.CheckEmailRequest;
import sixgaezzang.sidepeek.users.dto.request.CheckNicknameRequest;
import sixgaezzang.sidepeek.users.dto.request.SignUpRequest;
import sixgaezzang.sidepeek.users.dto.request.UpdatePasswordRequest;
import sixgaezzang.sidepeek.users.dto.request.UpdateUserProfileRequest;
import sixgaezzang.sidepeek.users.dto.response.CheckDuplicateResponse;
import sixgaezzang.sidepeek.users.dto.response.UserProfileResponse;
import sixgaezzang.sidepeek.users.dto.response.UserSearchResponse;

@Tag(name = "User", description = "사용자 API")
public interface UserControllerDoc {

    @Operation(summary = "회원가입")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CREATED", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters({
        @Parameter(name = "email", description = "이메일", example = "sidepeek6@gmail.com"),
        @Parameter(name = "password", description = "영문/숫자/특수문자 포함 8자 이상", example = "sidepeek6!"),
        @Parameter(name = "nickname", description = "닉네임", example = "육개짱"),
    })
    ResponseEntity<Void> signUp(SignUpRequest request);

    @Operation(summary = "비밀번호 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "NO_CONTENT"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> updatePassword(
        @Schema(description = "로그인한 회원 식별자(토큰에서 추출)") Long loginId,
        @Schema(description = "수정할 회원 식별자") Long id,
        UpdatePasswordRequest request
    );

    @Operation(summary = "이메일 중복 확인")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "email", description = "이메일", example = "sidepeek6@gmail.com")
    ResponseEntity<CheckDuplicateResponse> checkEmailDuplicate(CheckEmailRequest request);

    @Operation(summary = "닉네임 중복 확인")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "nickname", description = "닉네임", example = "육개짱")
    ResponseEntity<CheckDuplicateResponse> checkNicknameDuplicate(CheckNicknameRequest request);

    @Operation(summary = "회원 검색", description = "닉네임으로 회원을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "keyword", description = "검색어", example = "sixgaezzang6")
    ResponseEntity<UserSearchResponse> searchByNickname(String keyword);

    @Operation(summary = "회원 프로필 정보 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ApiResponse(responseCode = "200", description = "회원 프로필 정보 조회 성공")
    ResponseEntity<UserProfileResponse> getById(@Schema(description = "회원 식별자") Long id);

    @Operation(summary = "회원 프로필 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<UserProfileResponse> update(
        @Schema(description = "로그인한 회원 식별자(토큰에서 추출)") Long loginId,
        @Schema(description = "수정할 회원 식별자") Long id,
        UpdateUserProfileRequest request
    );
}
