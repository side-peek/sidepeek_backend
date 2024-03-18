package sixgaezzang.sidepeek.common.doc;

import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.BAD_REQUEST_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.BAD_REQUEST_DESCRIPTION1;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.BAD_REQUEST_DESCRIPTION2;
import static sixgaezzang.sidepeek.common.doc.description.ResponseCodeDescription.OK_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.SkillDescription.SKILL_KEYWORD_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.BAD_REQUEST_RESPONSE1;
import static sixgaezzang.sidepeek.common.doc.response.error.ErrorResponseDoc.BAD_REQUEST_RESPONSE2;
import static sixgaezzang.sidepeek.skill.domain.Skill.MAX_SKILL_NAME_LENGTH;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_NAME_OVER_MAX_LENGTH;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.skill.dto.response.SkillSearchResponse;

@Tag(name = "Skill", description = "기술스택 API")
public interface SkillControllerDoc {
    @Operation(summary = "기술 스택 검색", description = "로그인 선택")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = OK_DESCRIPTION,
            useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_DESCRIPTION,
            content = @Content(examples = {
                @ExampleObject(name = "Example1: One Field Error", description = BAD_REQUEST_DESCRIPTION1,
                    value = BAD_REQUEST_RESPONSE1),
                @ExampleObject(name = "Example2: Multiple Field Error", description = BAD_REQUEST_DESCRIPTION2,
                    value = BAD_REQUEST_RESPONSE2)}))
    })
    @Parameter(name = "keyword", description = SKILL_KEYWORD_DESCRIPTION, example = "spring", in = ParameterIn.QUERY)
    ResponseEntity<SkillSearchResponse> searchByName(
        @Size(max = MAX_SKILL_NAME_LENGTH, message = SKILL_NAME_OVER_MAX_LENGTH) String keyword
    );

}
