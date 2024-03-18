package sixgaezzang.sidepeek.common.doc;

import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.BAD_REQUEST_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.BAD_REQUEST_DESCRIPTION1;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.BAD_REQUEST_DESCRIPTION2;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.CREATED_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.FORBIDDEN_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.NOT_FOUND_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.NO_CONTENT_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.UNAUTHORIZED_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.BAD_REQUEST_RESPONSE1;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.BAD_REQUEST_RESPONSE2;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.FORBIDDEN_RESPONSE;
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
import sixgaezzang.sidepeek.comments.dto.request.SaveCommentRequest;
import sixgaezzang.sidepeek.comments.dto.request.UpdateCommentRequest;

@Tag(name = "Comment", description = "댓글 API")
public interface CommentControllerDoc {
    @Operation(summary = "댓글 생성", description = "로그인 필수")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = CREATED_DESCRIPTION),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_DESCRIPTION,
            content = @Content(examples = {
                @ExampleObject(name = "Example1: One Field Error", description = BAD_REQUEST_DESCRIPTION1,
                    value = BAD_REQUEST_RESPONSE1),
                @ExampleObject(name = "Example2: Multiple Field Error", description = BAD_REQUEST_DESCRIPTION2,
                    value = BAD_REQUEST_RESPONSE2)})),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = UNAUTHORIZED_RESPONSE)))
    })
    ResponseEntity<Void> save(@Parameter(hidden = true) Long loginId, SaveCommentRequest request);

    @Operation(summary = "댓글 수정", description = "로그인 필수")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = NO_CONTENT_DESCRIPTION),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_DESCRIPTION,
            content = @Content(examples = {
                @ExampleObject(name = "Example1: One Field Error", description = BAD_REQUEST_DESCRIPTION1,
                    value = BAD_REQUEST_RESPONSE1),
                @ExampleObject(name = "Example2: Multiple Field Error", description = BAD_REQUEST_DESCRIPTION2,
                    value = BAD_REQUEST_RESPONSE2)})),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = UNAUTHORIZED_RESPONSE))),
        @ApiResponse(responseCode = "403", description = FORBIDDEN_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = FORBIDDEN_RESPONSE))),
        @ApiResponse(responseCode = "404", description = NOT_FOUND_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = NOT_FOUND_RESPONSE)))
    })
    @Parameter(name = "id", description = "수정할 댓글 식별자", example = "1", in = ParameterIn.PATH)
    ResponseEntity<Void> update(@Parameter(hidden = true) Long loginId, Long commentId, UpdateCommentRequest request);

    @Operation(summary = "댓글 삭제", description = "로그인 필수")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = NO_CONTENT_DESCRIPTION),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = UNAUTHORIZED_RESPONSE))),
        @ApiResponse(responseCode = "403", description = FORBIDDEN_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = FORBIDDEN_RESPONSE))),
        @ApiResponse(responseCode = "404", description = NOT_FOUND_DESCRIPTION,
            content = @Content(examples = @ExampleObject(value = NOT_FOUND_RESPONSE)))
    })
    @Parameter(name = "id", description = "삭제할 댓글 식별자", example = "1", in = ParameterIn.PATH)
    ResponseEntity<Void> delete(@Parameter(hidden = true) Long loginId, Long commentId);

}
