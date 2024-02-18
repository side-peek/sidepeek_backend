package sixgaezzang.sidepeek.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 업로드")
    @ApiResponse(responseCode = "200", description = "파일 업로드 성공")
    public ResponseEntity<FileUploadResponse> uploadFile(
        @RequestParam
        MultipartFile file
    ) {
        return ResponseEntity.ok()
            .body(fileService.uploadFile(file));
    }

}
