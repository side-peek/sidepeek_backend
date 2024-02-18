package sixgaezzang.sidepeek.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sixgaezzang.sidepeek.file.dto.response.FileUploadResponse;
import sixgaezzang.sidepeek.file.service.FileService;

@RequestMapping("/files")
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<FileUploadResponse> uploadFile(
        @RequestParam
        MultipartFile file
    ) {
        return ResponseEntity.ok()
            .body(fileService.uploadFile(file));
    }

}
