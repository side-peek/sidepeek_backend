package sixgaezzang.sidepeek.comments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sixgaezzang.sidepeek.comments.dto.request.SaveCommentRequest;
import sixgaezzang.sidepeek.comments.dto.request.UpdateCommentRequest;
import sixgaezzang.sidepeek.comments.service.CommentService;
import sixgaezzang.sidepeek.common.annotation.Login;

@RestController
@RequestMapping("/comments")
@Tag(name = "Project Comment", description = "Project Comment API")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 생성")
    @ApiResponse(responseCode = "201", description = "댓글 생성 성공")
    public ResponseEntity<Void> save(
        @Schema(description = "로그인한 회원 식별자", example = "1")
        @Login
        Long loginId,

        @Valid
        @RequestBody
        SaveCommentRequest request
    ) {
        Long projectId = commentService.save(loginId, request);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/projects/{id}")
            .buildAndExpand(projectId).toUri();

        return ResponseEntity.created(uri)
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    public ResponseEntity<Void> update(
        @Schema(description = "로그인한 회원 식별자", example = "1")
        @Login
        Long loginId,

        @Schema(description = "수정할 댓글 식별자", example = "1")
        @PathVariable(value = "id")
        Long commentId,

        @Valid
        @RequestBody
        UpdateCommentRequest request
    ) {
        commentService.update(loginId, commentId, request);

        return ResponseEntity.noContent()
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제")
    @ApiResponse(responseCode = "204", description = "댓글 삭제 성공")
    public ResponseEntity<Void> delete(
        @Schema(description = "로그인한 회원 식별자", example = "1")
        @Login
        Long loginId,

        @Schema(description = "삭제할 댓글 식별자", example = "1")
        @PathVariable(value = "id")
        Long commentId
    ) {
        commentService.delete(loginId, commentId);

        return ResponseEntity.noContent()
            .build();
    }

}
