package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.projects.dto.request.ProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;

@Tag(name = "Project", description = "프로젝트 API")
public interface ProjectControllerDoc {

    @Operation(summary = "프로젝트 생성")
    @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공")
    ResponseEntity<ProjectResponse> save(
        @Parameter(description = "로그인한 회원 식별자", in = ParameterIn.HEADER)
        Long loginId,
        ProjectRequest request
    );

    @Operation(summary = "프로젝트 수정")
    @ApiResponse(responseCode = "200", description = "프로젝트 수정 성공(프로젝트 작성자/회원 멤버만 가능)")
    ResponseEntity<ProjectResponse> update(
        @Parameter(description = "로그인한 회원 식별자", in = ParameterIn.HEADER) Long loginId,
        @Parameter(description = "수정할 프로젝트 식별자", in = ParameterIn.PATH) Long projectId,
        ProjectRequest request
    );

    @Operation(summary = "프로젝트 삭제")
    @ApiResponse(responseCode = "204", description = "프로젝트 삭제 성공(프로젝트 작성자만 가능)")
    ResponseEntity<Void> delete(
        @Parameter(description = "로그인한 회원 식별자", in = ParameterIn.HEADER) Long loginId,
        @Parameter(description = "삭제할 프로젝트 식별자", in = ParameterIn.PATH) Long projectId
    );

    @Operation(summary = "프로젝트 상세 조회")
    @ApiResponse(responseCode = "200", description = "프로젝트 상세 조회 성공")
    ResponseEntity<ProjectResponse> getById(
        @Parameter(description = "조회할 프로젝트 식별자", in = ParameterIn.PATH)
        Long id
    );

    @Operation(summary = "프로젝트 전체 조회")
    @ApiResponse(responseCode = "200", description = "프로젝트 전체 조회 성공")
    @Parameters({
        @Parameter(name = "loginId", description = "로그인한 회원 식별자", in = ParameterIn.HEADER),
        @Parameter(name = "sort", description = "정렬 조건 [ createdAt(default), view, like ]", in = ParameterIn.QUERY),
        @Parameter(name = "isReleased", description = "출시서비스만 보기", in = ParameterIn.QUERY)
    })
    ResponseEntity<List<ProjectListResponse>> getAll(
        Long loginId,
        String sort,
        boolean isReleased
    );
}
