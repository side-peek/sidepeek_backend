package sixgaezzang.sidepeek.like.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<LikeResponse> toggle(
        @Login Long loginId,
        @Valid @RequestBody LikeRequest request
    ) {
        LikeResponse response = likeService.toggle(loginId, request);

        return ResponseEntity.ok(response);
    }

}
