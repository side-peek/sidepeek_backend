package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;
import sixgaezzang.sidepeek.media.dto.response.MediaUploadResponse;

@Tag(name = "Media", description = "미디어(파일 업로드) API")
public interface MediaControllerDoc {

    @Operation(summary = "파일 업로드")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<MediaUploadResponse> uploadFile(@Parameter(hidden = true) Long loginId,
        MultipartFile file);

}
