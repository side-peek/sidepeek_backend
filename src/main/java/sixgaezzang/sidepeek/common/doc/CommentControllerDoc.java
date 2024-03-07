package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.comments.dto.request.CommentRequest;
import sixgaezzang.sidepeek.comments.dto.response.CommentResponse;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;

@Tag(name = "Comment", description = "댓글 API")
public interface CommentControllerDoc {

    @Operation(summary = "댓글 생성")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CREATED", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<CommentResponse> save(
        @Schema(description = "로그인한 회원 식별자", example = "1") Long loginId,
        @Schema(description = "생성할 댓글의 프로젝트 식별자", example = "1") Long projectId,
        CommentRequest request
    );

    @Operation(summary = "댓글 수정")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<CommentResponse> update(
        @Schema(description = "로그인한 회원 식별자", example = "1") Long loginId,
        @Schema(description = "수정할 댓글의 프로젝트 식별자", example = "1") Long projectId,
        @Schema(description = "수정할 댓글 식별자", example = "1") Long commentId,
        CommentRequest request
    );

    @Operation(summary = "댓글 삭제")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "NO_CONTENT"),
        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "FORBIDDEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> delete(
        @Schema(description = "로그인한 회원 식별자", example = "1") Long loginId,
        @Schema(description = "삭제할 댓글의 프로젝트 식별자", example = "1") Long projectId,
        @Schema(description = "삭제할 댓글 식별자", example = "1") Long commentId
    );

}
