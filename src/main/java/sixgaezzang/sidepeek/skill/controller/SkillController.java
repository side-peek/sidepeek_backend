package sixgaezzang.sidepeek.skill.controller;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sixgaezzang.sidepeek.skill.dto.SkillSearchResponse;
import sixgaezzang.sidepeek.skill.serivce.SkillService;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<SkillSearchResponse> searchByName(
        @RequestParam(required = false)
        @Size(max = 50, message = "최대 50자의 키워드로 검색할 수 있습니다.")
        String keyword
    ) {
        return ResponseEntity.ok()
            .body(skillService.searchByName(keyword));
    }

}
