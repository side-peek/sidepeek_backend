package sixgaezzang.sidepeek.projects.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sixgaezzang.sidepeek.projects.dto.request.ProjectSaveRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.service.ProjectService;

@RestController
@RequestMapping("/projects")
@Tag(name = "Project", description = "Project API")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ProjectSaveRequest request) {
        Long id = projectService.save(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/projects/{id}")
            .buildAndExpand(id).toUri();

        return ResponseEntity.created(uri)
            .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "프로젝트 상세 조회")
    @ApiResponse(responseCode = "200", description = "프로젝트 상세 조회 성공")
    public ResponseEntity<ProjectResponse> getById(
        @PathVariable Long id
    ) {
        ProjectResponse response = projectService.findById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "프로젝트 전체 조회")
    @ApiResponse(responseCode = "200", description = "프로젝트 전체 조회 성공")
    @Parameters({
        @Parameter(name = "userId", description = "로그인한 사용자의 ID"),
        @Parameter(name = "sort", description = "정렬 조건 [ createdAt(default), view, like ]"),
        @Parameter(name = "isReleased", description = "출시서비스만 보기")
    })
    public ResponseEntity<List<ProjectListResponse>> getAll(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false, defaultValue = "false") boolean isReleased
    ) {
        sort = (sort == null) ? "createdAt" : sort;
        List<ProjectListResponse> responses = projectService.findAll(userId, sort, isReleased);
        //TODO: @Login 어노테이션에서 requried=false를 활용할 순 없을까? (로그인한 사용자가 좋아요한 프로젝트를 확인하기 위해)

        return ResponseEntity.ok(responses);
    }
}
