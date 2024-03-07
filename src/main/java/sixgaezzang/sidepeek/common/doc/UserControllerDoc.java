package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
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
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    @Parameters({
        @Parameter(name = "email", description = "이메일", example = "sidepeek6@gmail.com"),
        @Parameter(name = "password", description = "영문/숫자/특수문자 포함 8자 이상", example = "sidepeek6!"),
        @Parameter(name = "nickname", description = "닉네임", example = "육개짱"),
    })
    ResponseEntity<Void> signUp(SignUpRequest request);

    @Operation(summary = "비밀번호 수정")
    @ApiResponse(responseCode = "204", description = "비밀번호 수정 성공")
    ResponseEntity<Void> updatePassword(
        @Schema(description = "로그인한 회원 식별자(토큰에서 추출)") Long loginId,
        @Schema(description = "수정할 회원 식별자") Long id,
        UpdatePasswordRequest request
    );

    @Operation(summary = "이메일 중복 확인")
    @ApiResponse(responseCode = "200", description = "이메일 중복 확인 성공")
    @Parameter(name = "email", description = "이메일", example = "sidepeek6@gmail.com")
    ResponseEntity<CheckDuplicateResponse> checkEmailDuplicate(CheckEmailRequest request);

    @Operation(summary = "닉네임 중복 확인")
    @ApiResponse(responseCode = "200", description = "닉네임 중복 확인 성공")
    @Parameter(name = "nickname", description = "닉네임", example = "육개짱")
    ResponseEntity<CheckDuplicateResponse> checkNicknameDuplicate(CheckNicknameRequest request);

    @Operation(summary = "회원 검색", description = "닉네임으로 회원을 검색합니다.")
    @ApiResponse(responseCode = "200", description = "회원 검색 성공")
    @Parameter(name = "keyword", description = "검색어", example = "sixgaezzang6")
    ResponseEntity<UserSearchResponse> searchByNickname(String keyword);

    @Operation(summary = "회원 프로필 정보 조회")
    @ApiResponse(responseCode = "200", description = "회원 프로필 정보 조회 성공")
    ResponseEntity<UserProfileResponse> getById(@Schema(description = "회원 식별자") Long id);

    @Operation(summary = "회원 프로필 정보 수정")
    @ApiResponse(responseCode = "200", description = "회원 프로필 정보 조회 성공")
    ResponseEntity<UserProfileResponse> update(
        @Schema(description = "로그인한 회원 식별자(토큰에서 추출)") Long loginId,
        @Schema(description = "수정할 회원 식별자") Long id,
        UpdateUserProfileRequest request
    );
}
