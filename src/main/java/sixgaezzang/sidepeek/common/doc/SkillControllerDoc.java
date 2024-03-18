package sixgaezzang.sidepeek.common.doc;

import static sixgaezzang.sidepeek.common.doc.response.ResponseCodeDescription.BAD_REQUEST_400_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.response.ResponseCodeDescription.OK_200_DESCRIPTION;
import static sixgaezzang.sidepeek.common.doc.description.SkillDescription.SKILL_KEYWORD_DESCRIPTION;
import static sixgaezzang.sidepeek.skill.domain.Skill.MAX_SKILL_NAME_LENGTH;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_NAME_OVER_MAX_LENGTH;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.common.exception.ErrorResponse;
import sixgaezzang.sidepeek.skill.dto.response.SkillSearchResponse;

@Tag(name = "Skill", description = "기술스택 API")
public interface SkillControllerDoc {
    @Operation(summary = "기술 스택 검색", description = "로그인 선택")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = OK_200_DESCRIPTION, useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST_400_DESCRIPTION, content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameter(name = "keyword", description = SKILL_KEYWORD_DESCRIPTION, example = "spring", in = ParameterIn.QUERY)
    ResponseEntity<SkillSearchResponse> searchByName(
        @Size(max = MAX_SKILL_NAME_LENGTH, message = SKILL_NAME_OVER_MAX_LENGTH) String keyword
    );

}
