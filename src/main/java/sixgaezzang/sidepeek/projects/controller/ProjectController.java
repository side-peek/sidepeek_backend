package sixgaezzang.sidepeek.projects.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.projects.dto.request.CursorPaginationInfoRequest;
import sixgaezzang.sidepeek.projects.dto.response.CursorPaginationResponse;
import sixgaezzang.sidepeek.common.doc.ProjectControllerDoc;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.service.ProjectService;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController implements ProjectControllerDoc {

    private final ProjectService projectService;

    @Override
    @PostMapping
    public ResponseEntity<ProjectResponse> save(
        @Login Long loginId,
        @Valid @RequestBody SaveProjectRequest request
    ) {
        ProjectResponse response = projectService.save(loginId, null, request);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/projects/{id}")
            .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri)
            .body(response);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(
        @Login Long loginId,
        @PathVariable(value = "id") Long projectId,
        @Valid @RequestBody SaveProjectRequest request
    ) {
        ProjectResponse response = projectService.save(loginId, projectId, request);

        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Login Long loginId,
        @PathVariable(value = "id") Long projectId
    ) {
        projectService.delete(loginId, projectId);

        return ResponseEntity.noContent()
            .build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(
        @PathVariable Long id
    ) {
        ProjectResponse response = projectService.findById(id);

        return ResponseEntity.ok(response);
    }

//    @GetMapping
//    @Operation(summary = "프로젝트 전체 조회")
//    @ApiResponse(responseCode = "200", description = "프로젝트 전체 조회 성공")
//    @Parameters({
//        @Parameter(name = "sort", description = "정렬 조건 [ latest(최신순), view(조회수순), like(좋아요순) ]", in = ParameterIn.QUERY),
//        @Parameter(name = "isReleased", description = "출시서비스만 보기", in = ParameterIn.QUERY)
//    })
//    public ResponseEntity<Slice<ProjectListResponse>> getAll(
//        @Login Long loginId,
//        @RequestParam(required = false) String sort,
//        @RequestParam(required = false, defaultValue = "false") boolean isReleased,
//        @RequestParam(required = false) Long lastProjectId,
//        @RequestParam(required = false) Long lastOrderNum,
//        @RequestParam(required = false) Integer pageSize
//    ) {
//        sort = (sort == null) ? "createdAt" : sort;
//        pageSize = (pageSize == null) ? 24 : pageSize;
//        Slice<ProjectListResponse> responses = projectService.findAll(loginId, sort, isReleased,
//            lastProjectId, lastOrderNum, pageSize);
//
//        return ResponseEntity.ok(responses);
//    }

//    @GetMapping
//    @Operation(summary = "프로젝트 전체 조회")
//    @ApiResponse(responseCode = "200", description = "프로젝트 전체 조회 성공")
//    @Parameters({
//        @Parameter(name = "sort", description = "정렬 조건 [ latest(최신순), view(조회수순), like(좋아요순) ]", in = ParameterIn.QUERY),
//        @Parameter(name = "isReleased", description = "출시서비스만 보기", in = ParameterIn.QUERY)
//    })
//    public ResponseEntity<ProjectListResponse> getAll(
//        @Login Long loginId,
//        @RequestParam(required = false) String sort,
//        @RequestParam(required = false, defaultValue = "false") boolean isReleased,
//        @RequestParam(required = false) Long lastProjectId,
//        @RequestParam(required = false) Long lastOrderNum,
//        @RequestParam(required = false) Integer pageSize
//    ) {
//        sort = (sort == null) ? "createdAt" : sort;
//        pageSize = (pageSize == null) ? 2 : pageSize;
//        ProjectListResponse responses = projectService.findAll(loginId, sort, isReleased,
//            lastProjectId, lastOrderNum, pageSize);
//
//        return ResponseEntity.ok(responses);
//    }

//    @GetMapping
//    @Operation(summary = "프로젝트 전체 조회")
//    @ApiResponse(responseCode = "200", description = "프로젝트 전체 조회 성공")
//    public ResponseEntity<List<ProjectListResponse>> getAll(
//        @Login Long loginId,
//        @RequestParam(required = false) String sort,
//        @RequestParam(required = false, defaultValue = "false") boolean isReleased
//    ) {
////        List<ProjectListResponse> responses = projectService.findAll(loginId, sort, isReleased);
//
//        return ResponseEntity.ok().build();
//    }

    @GetMapping
    public ResponseEntity<CursorPaginationResponse<ProjectListResponse>> getByCondition(
        @Login Long loginId,
        @Valid @ModelAttribute CursorPaginationInfoRequest pageable
    ) {
        CursorPaginationResponse<ProjectListResponse> responses = projectService.findByCondition(
            loginId, pageable);
        return ResponseEntity.ok().body(responses);
    }
}
