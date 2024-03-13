package sixgaezzang.sidepeek.projects.controller;

import jakarta.validation.Valid;
import java.net.URI;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.common.doc.ProjectControllerDoc;
import sixgaezzang.sidepeek.projects.dto.request.CursorPaginationInfoRequest;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.request.UpdateProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectBannerResponse;
import sixgaezzang.sidepeek.projects.dto.response.CursorPaginationResponse;
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
        ProjectResponse response = projectService.save(loginId, request);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/projects/{id}")
            .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri)
            .body(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(
        @PathVariable Long id
    ) {
        ProjectResponse response = projectService.findById(id);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ProjectListResponse>> getAll(
        @Login Long loginId,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false, defaultValue = "false") boolean isReleased
    ) {
        sort = (sort == null) ? "createdAt" : sort;
        List<ProjectListResponse> responses = projectService.findAll(loginId, sort, isReleased);

        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/weekly")
    public ResponseEntity<List<ProjectBannerResponse>> getAllPopularThisWeek() {
        List<ProjectBannerResponse> responses = projectService.findAllPopularLastWeek();

        return ResponseEntity.ok(responses);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(
        @Login Long loginId,
        @PathVariable(value = "id") Long projectId,
        @Valid @RequestBody UpdateProjectRequest request
    ) {
        ProjectResponse response = projectService.update(loginId, projectId, request);

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

    @Override
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
