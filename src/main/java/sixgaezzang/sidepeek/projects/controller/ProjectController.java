package sixgaezzang.sidepeek.projects.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.service.ProjectService;

@RestController
@RequestMapping("/projects")
@Tag(name = "Project", description = "Project API")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "프로젝트 생성")
    @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공")
    public ResponseEntity<ProjectResponse> save(
        @Parameter(description = "로그인한 회원 식별자", in = ParameterIn.HEADER)
        @Login
        Long loginId,

        @Valid
        @RequestBody
        SaveProjectRequest request
    ) {
        ProjectResponse response = projectService.save(loginId, null, request);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/projects/{id}")
            .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri)
            .body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "프로젝트 수정")
    @ApiResponse(responseCode = "200", description = "프로젝트 수정 성공(프로젝트 작성자/회원 멤버만 가능)")
    public ResponseEntity<ProjectResponse> update(
        @Parameter(description = "로그인한 회원 식별자", in = ParameterIn.HEADER)
        @Login
        Long loginId,

        @Parameter(description = "수정할 프로젝트 식별자", in = ParameterIn.PATH)
        @PathVariable(value = "id")
        Long projectId,

        @Valid
        @RequestBody
        SaveProjectRequest request
    ) {
        ProjectResponse response = projectService.save(loginId, projectId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "프로젝트 삭제")
    @ApiResponse(responseCode = "204", description = "프로젝트 삭제 성공(프로젝트 작성자만 가능)")
    public ResponseEntity<Void> delete(
        @Parameter(description = "로그인한 회원 식별자", in = ParameterIn.HEADER)
        @Login
        Long loginId,

        @Parameter(description = "삭제할 프로젝트 식별자", in = ParameterIn.PATH)
        @PathVariable(value = "id")
        Long projectId
    ) {
        projectService.delete(loginId, projectId);

        return ResponseEntity.noContent()
            .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "프로젝트 상세 조회")
    @ApiResponse(responseCode = "200", description = "프로젝트 상세 조회 성공")
    public ResponseEntity<ProjectResponse> getById(
        @Parameter(description = "조회할 프로젝트 식별자", in = ParameterIn.PATH)
        @PathVariable
        Long id
    ) {
        ProjectResponse response = projectService.findById(id);

        return ResponseEntity.ok(response);
    }

}
