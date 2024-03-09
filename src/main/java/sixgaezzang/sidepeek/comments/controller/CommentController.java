package sixgaezzang.sidepeek.comments.controller;

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
import sixgaezzang.sidepeek.common.doc.CommentControllerDoc;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController implements CommentControllerDoc {

    @Override
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> save(
        @Login Long loginId,
        @PathVariable Long projectId,
        @Valid @RequestBody SaveCommentRequest request
    ) {
        Long projectId = commentService.save(loginId, request);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/projects/{id}")
            .buildAndExpand(projectId).toUri();

        return ResponseEntity.created(uri)
            .build();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
        @Login Long loginId,
        @PathVariable Long projectId,
        @PathVariable(value = "id") Long commentId,
        @Valid @RequestBody UpdateCommentRequest request
    ) {
        commentService.update(loginId, commentId, request);

        return ResponseEntity.noContent()
            .build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Login Long loginId,
        @PathVariable Long projectId,
        @PathVariable(value = "id") Long commentId
    ) {
        commentService.delete(loginId, commentId);

        return ResponseEntity.noContent()
            .build();
    }

}
