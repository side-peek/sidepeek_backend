package sixgaezzang.sidepeek.like.controller;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.common.doc.LikeControllerDoc;
import sixgaezzang.sidepeek.like.dto.request.LikeRequest;
import sixgaezzang.sidepeek.like.dto.response.LikeResponse;
import sixgaezzang.sidepeek.like.service.LikeService;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController implements LikeControllerDoc {

    private final LikeService likeService;

    @Override
    @PostMapping
    public ResponseEntity<LikeResponse> save(
        @Login Long loginId,
        @Valid @RequestBody LikeRequest request
    ) {
        LikeResponse response = likeService.save(loginId, request);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("projects/{id}")
            .buildAndExpand(response.projectId()).toUri();

        return ResponseEntity.created(uri)
            .body(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Login Long loginId,
        @PathVariable(value = "id") Long likeId
    ) {
        likeService.delete(loginId, likeId);

        return ResponseEntity.noContent()
            .build();
    }

}
