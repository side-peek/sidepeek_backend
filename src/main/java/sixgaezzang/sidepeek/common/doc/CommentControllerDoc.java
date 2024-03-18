package sixgaezzang.sidepeek.common.doc;

import static sixgaezzang.sidepeek.common.doc.description.response.ResponseCodeDescription.BAD_REQUEST_400_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.response.ResponseCodeDescription.CREATED_201_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.response.ResponseCodeDescription.FORBIDDEN_403_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.response.ResponseCodeDescription.NOT_FOUND_404_DESCRIPTION;
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
import sixgaezzang.sidepeek.comments.dto.request.SaveCommentRequest;
import sixgaezzang.sidepeek.comments.dto.request.UpdateCommentRequest;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;

@Tag(name = "Comment", description = "댓글 API")
public interface CommentControllerDoc {
    @Operation(summary = "댓글 생성", description = "로그인 필수")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = CREATED_201_DESCRIPTION),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_400_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_401_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> save(@Parameter(hidden = true) Long loginId, SaveCommentRequest request);

    @Operation(summary = "댓글 수정", description = "로그인 필수")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "NO_CONTENT"),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_400_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_401_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = FORBIDDEN_403_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = NOT_FOUND_404_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "수정할 댓글 식별자", example = "1", in = ParameterIn.PATH)
    ResponseEntity<Void> update(@Parameter(hidden = true) Long loginId, Long commentId, UpdateCommentRequest request);

    @Operation(summary = "댓글 삭제", description = "로그인 필수")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "NO_CONTENT"),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_401_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = FORBIDDEN_403_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = NOT_FOUND_404_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "id", description = "삭제할 댓글 식별자", example = "1", in = ParameterIn.PATH)
    ResponseEntity<Void> delete(@Parameter(hidden = true) Long loginId, Long commentId);

}
