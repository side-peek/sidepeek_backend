package sixgaezzang.sidepeek.skill.controller;

import static sixgaezzang.sidepeek.skill.domain.Skill.MAX_SKILL_NAME_LENGTH;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sixgaezzang.sidepeek.skill.dto.response.SkillSearchResponse;
import sixgaezzang.sidepeek.skill.serivce.SkillService;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    @Operation(summary = "기술 스택 검색")
    @ApiResponse(responseCode = "200", description = "기술 스택 검색 성공")
    @Parameter(name = "keyword", description = "검색어", example = "spring")
    public ResponseEntity<SkillSearchResponse> searchByName(
        @RequestParam(required = false)
        @Size(max = MAX_SKILL_NAME_LENGTH, message = "최대 " + MAX_SKILL_NAME_LENGTH + "자의 키워드로 검색할 수 있습니다.")
        String keyword
    ) {
        return ResponseEntity.ok()
            .body(skillService.searchByName(keyword));
    }

}
