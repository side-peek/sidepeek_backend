package sixgaezzang.sidepeek.common.doc;

import static sixgaezzang.sidepeek.common.doc.description.response.ResponseCodeDescription.BAD_REQUEST_400_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.response.ResponseCodeDescription.CONFLICT_409_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.response.ResponseCodeDescription.NOT_FOUND_404_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.response.ResponseCodeDescription.OK_200_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.response.ResponseCodeDescription.UNAUTHORIZED_401_DESCRIPTION;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;
import sixgaezzang.sidepeek.like.dto.request.LikeRequest;
import sixgaezzang.sidepeek.like.dto.response.LikeResponse;

@Tag(name = "Like", description = "좋아요 API")
public interface LikeControllerDoc {

    @Operation(summary = "좋아요", description = "로그인 필수")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = OK_200_DESCRIPTION, useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_400_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_401_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = NOT_FOUND_404_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = CONFLICT_409_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<LikeResponse> save(@Parameter(hidden = true) Long loginId, LikeRequest request);

    @Operation(summary = "좋아요 취소", description = "로그인 필수")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = OK_200_DESCRIPTION, useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_400_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_401_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = NOT_FOUND_404_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "likeId", description = "삭제할 좋아요 식별자", example = "1", in = ParameterIn.PATH)
    ResponseEntity<Void> delete(@Parameter(hidden = true) Long loginId, Long likeId);

}
