package sixgaezzang.sidepeek.common.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import sixgaezzang.sidepeek.skill.dto.response.SkillSearchResponse;

public interface SkillControllerDoc {

    @Operation(summary = "기술 스택 검색")
    @ApiResponse(responseCode = "200", description = "기술 스택 검색 성공")
    @Parameter(name = "keyword", description = "검색어", example = "spring")
    ResponseEntity<SkillSearchResponse> searchByName(
        String keyword
    );

}
