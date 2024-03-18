package sixgaezzang.sidepeek.common.doc;

import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.BAD_REQUEST_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.INTERNAL_SERVER_ERROR_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.OK_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.UNAUTHORIZED_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.BAD_REQUEST_RESPONSE;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.INTERNAL_SERVER_ERROR_RESPONSE;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.UNAUTHORIZED_RESPONSE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import sixgaezzang.sidepeek.media.dto.response.MediaUploadResponse;

@Tag(name = "Media", description = "미디어(파일 업로드) API")
public interface MediaControllerDoc {

    @Operation(summary = "파일 업로드", description = "로그인 필수")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = OK_DESCRIPTION,
            useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = BAD_REQUEST_RESPONSE))),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = UNAUTHORIZED_RESPONSE))),
        @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = INTERNAL_SERVER_ERROR_RESPONSE)))
    })
    ResponseEntity<MediaUploadResponse> uploadFile(@Parameter(hidden = true) Long loginId,
        MultipartFile file);

}
