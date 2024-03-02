package sixgaezzang.sidepeek.comments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import sixgaezzang.sidepeek.comments.dto.request.CommentRequest;
import sixgaezzang.sidepeek.comments.dto.response.CommentResponse;
import sixgaezzang.sidepeek.comments.dto.response.CommentListResponse;
import sixgaezzang.sidepeek.common.annotation.Login;

@RestController
@RequestMapping("/projects/{projectId}/comments")
@Tag(name = "Project Comment", description = "Project Comment API")
@RequiredArgsConstructor
public class CommentController {

    @PostMapping
    @Operation(summary = "댓글 생성")
    @ApiResponse(responseCode = "201", description = "댓글 생성 성공")
    public ResponseEntity<CommentResponse> save(
        @Schema(description = "로그인한 회원 식별자", example = "1")
        @Login
        Long loginId,

        @Schema(description = "생성할 댓글의 프로젝트 식별자", example = "1")
        @PathVariable
        Long projectId,

        @Valid
        @RequestBody
        CommentRequest request
    ) {
        return null;
    }

    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    public ResponseEntity<CommentResponse> update(
        @Schema(description = "로그인한 회원 식별자", example = "1")
        @Login
        Long loginId,

        @Schema(description = "수정할 댓글의 프로젝트 식별자", example = "1")
        @PathVariable
        Long projectId,

        @Schema(description = "수정할 댓글 식별자", example = "1")
        @PathVariable(value = "id")
        Long commentId,

        @Valid
        @RequestBody
        CommentRequest request
    ) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제")
    @ApiResponse(responseCode = "204", description = "댓글 삭제 성공")
    public ResponseEntity<Void> delete(
        @Schema(description = "로그인한 회원 식별자", example = "1")
        @Login
        Long loginId,

        @Schema(description = "삭제할 댓글의 프로젝트 식별자", example = "1")
        @PathVariable
        Long projectId,

        @Schema(description = "삭제할 댓글 식별자", example = "1")
        @PathVariable(value = "id")
        Long commentId
    ) {
        return null;
    }

    @GetMapping
    @Operation(summary = "댓글 목록 조회")
    @ApiResponse(responseCode = "204", description = "댓글 목록 조회 성공")
    public ResponseEntity<CommentListResponse> findAllByProject(
        @Schema(description = "조화할 댓글들의 프로젝트 식별자", example = "1")
        @PathVariable
        Long projectId
    ) {
        return null;
    }

}
