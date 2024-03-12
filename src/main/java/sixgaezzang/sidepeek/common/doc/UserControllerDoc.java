package sixgaezzang.sidepeek.common.doc;

import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.NICKNAME_OVER_MAX_LENGTH;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;
import sixgaezzang.sidepeek.projects.domain.UserProjectSearchType;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
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
    ResponseEntity<Void> signUp(@Valid SignUpRequest request);

    @Operation(summary = "비밀번호 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "NO_CONTENT"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "수정할 회원 식별자", example = "1")
    ResponseEntity<Void> updatePassword(@Parameter(hidden = true) Long loginId, Long id,
                                        @Valid UpdatePasswordRequest request);

    @Operation(summary = "이메일 중복 확인")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<CheckDuplicateResponse> checkEmailDuplicate(@Valid CheckEmailRequest request);

    @Operation(summary = "닉네임 중복 확인")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<CheckDuplicateResponse> checkNicknameDuplicate(@Valid CheckNicknameRequest request);

    @Operation(summary = "회원 검색", description = "닉네임으로 회원을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "keyword", description = "검색어", example = "sixgaezzang6", in = ParameterIn.QUERY)
    ResponseEntity<UserSearchResponse> searchByNickname(
        @Size(max = MAX_NICKNAME_LENGTH, message = NICKNAME_OVER_MAX_LENGTH)
        String keyword
    );

    @Operation(summary = "회원 프로필 정보 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "조회할 회원 식별자", example = "1", in = ParameterIn.PATH)
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
                                               @Valid UpdateUserProfileRequest request);

    @Operation(summary = "회원 프로젝트 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters({
        @Parameter(name = "id", description = "조회할 회원 식별자", example = "1", in = ParameterIn.PATH),
        @Parameter(name = "type", description = "프로젝트 조회 타입", in = ParameterIn.QUERY),
        @Parameter(name = "page", description = "조회할 페이지 번호 (기본값: 0)", in = ParameterIn.QUERY),
        @Parameter(name = "size", description = "한 페이지의 크기 (기본값: 12)", in = ParameterIn.QUERY)
    })
    ResponseEntity<Page<ProjectListResponse>> getProjects(
        @Parameter(hidden = true) Long loginId,
        Long id, UserProjectSearchType type, @Parameter(hidden = true) Pageable pageable);
}
