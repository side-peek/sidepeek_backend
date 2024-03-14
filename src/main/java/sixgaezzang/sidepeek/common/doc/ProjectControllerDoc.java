package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;
import sixgaezzang.sidepeek.projects.dto.request.CursorPaginationInfoRequest;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.request.UpdateProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.CursorPaginationResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectBannerResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;

@Tag(name = "Project", description = "프로젝트 API")
public interface ProjectControllerDoc {

    @Operation(summary = "프로젝트 생성", description = "로그인 필수")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CREATED", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<ProjectResponse> save(@Parameter(hidden = true) Long loginId,
                                         SaveProjectRequest request);

    @Operation(summary = "프로젝트 상세 조회", description = "프로젝트 하나의 상세 정보 조회, 로그인 선택")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "조회할 프로젝트 식별자", in = ParameterIn.PATH)
    ResponseEntity<ProjectResponse> getById(Long id);

    @Operation(summary = "프로젝트 전체 조회", description = "프로젝트 게시글 목록을 조건에 따라 조회, 로그인 선택")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true)
    })
    ResponseEntity<CursorPaginationResponse<ProjectListResponse>> getByCondition(
        @Parameter(hidden = true) Long loginId,
        @Valid @ModelAttribute CursorPaginationInfoRequest pageable);

    @Operation(summary = "지난 주 인기 프로젝트 조회", description = "지난 주 좋아요를 많이 받은 순으로 최대 5개 프로젝트 목록 조회, 로그인 선택")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true)
    })
    ResponseEntity<List<ProjectBannerResponse>> getAllPopularThisWeek();

    @Operation(summary = "프로젝트 수정", description = "작성자와 등록된 프로젝트 회원 멤버만 수정 가능, 로그인 필수")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "수정할 프로젝트 식별자", in = ParameterIn.PATH)
    ResponseEntity<ProjectResponse> update(@Parameter(hidden = true) Long loginId, Long projectId,
                                           UpdateProjectRequest request);

    @Operation(summary = "프로젝트 삭제", description = "프로젝트 작성자만 삭제 가능, 로그인 필수")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "NO_CONTENT"),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "삭제할 프로젝트 식별자", in = ParameterIn.PATH)
    ResponseEntity<Void> delete(@Parameter(hidden = true) Long loginId, Long projectId);

}
