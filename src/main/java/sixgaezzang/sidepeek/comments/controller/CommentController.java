package sixgaezzang.sidepeek.comments.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sixgaezzang.sidepeek.comments.dto.request.CommentRequest;
import sixgaezzang.sidepeek.comments.dto.response.CommentResponse;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.common.doc.CommentControllerDoc;

@RestController
@RequestMapping("/projects/{projectId}/comments")
@RequiredArgsConstructor
public class CommentController implements CommentControllerDoc {

    @Override
    @PostMapping
    public ResponseEntity<CommentResponse> save(
        @Login Long loginId,
        @PathVariable Long projectId,
        @Valid @RequestBody CommentRequest request
    ) {
        return null;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(
        @Login Long loginId,
        @PathVariable Long projectId,
        @PathVariable(value = "id") Long commentId,
        CommentRequest request
    ) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Login Long loginId,
        @PathVariable Long projectId,
        @PathVariable(value = "id") Long commentId
    ) {
        return null;
    }

}
