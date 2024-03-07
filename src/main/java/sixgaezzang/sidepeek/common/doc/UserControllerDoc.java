package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
    ResponseEntity<Void> signUp(SignUpRequest request);

    @Operation(summary = "비밀번호 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "NO_CONTENT"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "수정할 회원 ID", example = "1")
    ResponseEntity<Void> updatePassword(@Parameter(hidden = true) Long loginId, Long id,
        UpdatePasswordRequest request);

    @Operation(summary = "이메일 중복 확인")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<CheckDuplicateResponse> checkEmailDuplicate(CheckEmailRequest request);

    @Operation(summary = "닉네임 중복 확인")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<CheckDuplicateResponse> checkNicknameDuplicate(CheckNicknameRequest request);

    @Operation(summary = "회원 검색", description = "닉네임으로 회원을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "keyword", description = "검색어", example = "sixgaezzang6", in = ParameterIn.QUERY)
    ResponseEntity<UserSearchResponse> searchByNickname(String keyword);

    @Operation(summary = "회원 프로필 정보 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "조회할 회원 ID", example = "1", in = ParameterIn.PATH)
    ResponseEntity<UserProfileResponse> getById(Long id);

    @Operation(summary = "회원 프로필 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "수정할 회원 식별자", example = "1", in = ParameterIn.PATH)
    ResponseEntity<UserProfileResponse> update(@Parameter(hidden = true) Long loginId, Long id,
        UpdateUserProfileRequest request);
}
