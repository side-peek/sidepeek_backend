package sixgaezzang.sidepeek.common.doc;

import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.BAD_REQUEST_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.CONFLICT_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.NOT_FOUND_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.OK_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.UNAUTHORIZED_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.BAD_REQUEST_RESPONSE;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.CONFLICT_RESPONSE;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.NOT_FOUND_RESPONSE;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.UNAUTHORIZED_RESPONSE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.like.dto.request.LikeRequest;
import sixgaezzang.sidepeek.like.dto.response.LikeResponse;

@Tag(name = "Like", description = "좋아요 API")
public interface LikeControllerDoc {

    @Operation(summary = "좋아요", description = "로그인 필수")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = OK_DESCRIPTION,
            useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = BAD_REQUEST_RESPONSE))),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = UNAUTHORIZED_RESPONSE))),
        @ApiResponse(responseCode = "404", description = NOT_FOUND_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = NOT_FOUND_RESPONSE))),
        @ApiResponse(responseCode = "409", description = CONFLICT_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = CONFLICT_RESPONSE)))
    })
    ResponseEntity<LikeResponse> save(@Parameter(hidden = true) Long loginId, LikeRequest request);

    @Operation(summary = "좋아요 취소", description = "로그인 필수")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = OK_DESCRIPTION,
            useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = BAD_REQUEST_RESPONSE))),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = UNAUTHORIZED_RESPONSE))),
        @ApiResponse(responseCode = "404", description = NOT_FOUND_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = NOT_FOUND_RESPONSE)))
    })
    @Parameter(name = "likeId", description = "삭제할 좋아요 식별자", example = "1", in = ParameterIn.PATH)
    ResponseEntity<Void> delete(@Parameter(hidden = true) Long loginId, Long likeId);

}
