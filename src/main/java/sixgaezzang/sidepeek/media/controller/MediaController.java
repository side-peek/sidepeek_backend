package sixgaezzang.sidepeek.media.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sixgaezzang.sidepeek.common.annotation.Login;
import sixgaezzang.sidepeek.common.doc.MediaControllerDoc;
import sixgaezzang.sidepeek.media.dto.response.MediaUploadResponse;
import sixgaezzang.sidepeek.media.service.MediaService;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class MediaController implements MediaControllerDoc {

    private final MediaService mediaService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaUploadResponse> uploadFile(
        @Login Long loginId,
        @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok()
            .body(mediaService.uploadFile(loginId, file));
    }

}
