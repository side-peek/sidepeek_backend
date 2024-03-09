package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;

@Tag(name = "Project", description = "프로젝트 API")
public interface ProjectControllerDoc {

    @Operation(summary = "프로젝트 생성")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CREATED", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<ProjectResponse> save(@Parameter(hidden = true) Long loginId,
                                         SaveProjectRequest request);

    @Operation(summary = "프로젝트 수정", description = "프로젝트 작성자와 멤버만 수정이 가능합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "수정할 프로젝트 식별자", in = ParameterIn.PATH)
    ResponseEntity<ProjectResponse> update(@Parameter(hidden = true) Long loginId, Long projectId,
                                           SaveProjectRequest request);

    @Operation(summary = "프로젝트 삭제", description = "프로젝트 작성자만 삭제가 가능합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "NO_CONTENT"),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "삭제할 프로젝트 식별자", in = ParameterIn.PATH)
    ResponseEntity<Void> delete(@Parameter(hidden = true) Long loginId, Long projectId);

    @Operation(summary = "프로젝트 상세 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "조회할 프로젝트 식별자", in = ParameterIn.PATH)
    ResponseEntity<ProjectResponse> getById(Long id);

    @Operation(summary = "프로젝트 전체 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true)
    })
    @Parameters({
        @Parameter(name = "sort", description = "정렬 조건 [ createdAt(default), view, like ]", in = ParameterIn.QUERY),
        @Parameter(name = "isReleased", description = "출시 서비스만 보기", in = ParameterIn.QUERY)
    })
    ResponseEntity<List<ProjectListResponse>> getAll(@Parameter(hidden = true) Long loginId,
        String sort, boolean isReleased);
}
