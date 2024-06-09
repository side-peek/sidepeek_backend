package sixgaezzang.sidepeek.skill.controller;

import static sixgaezzang.sidepeek.skill.domain.Skill.MAX_SKILL_NAME_LENGTH;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_NAME_OVER_MAX_LENGTH;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sixgaezzang.sidepeek.common.doc.SkillControllerDoc;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.dto.response.SkillSearchResponse;
import sixgaezzang.sidepeek.skill.serivce.SkillService;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController implements SkillControllerDoc {

    private final SkillService skillService;

    @Override
    @GetMapping
    public ResponseEntity<SkillSearchResponse> searchByName(
        @Size(max = MAX_SKILL_NAME_LENGTH, message = SKILL_NAME_OVER_MAX_LENGTH)
        @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok()
            .body(skillService.searchByName(keyword));
    }

    @GetMapping("/offset")
    public ResponseEntity<Slice<Skill>> searchByNameOffset(
        Pageable pageable,
        @Size(max = MAX_SKILL_NAME_LENGTH, message = SKILL_NAME_OVER_MAX_LENGTH)
        @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok()
            .body(skillService.searchByNameOffset(keyword, pageable));
    }

    @GetMapping("/no-offset")
    public ResponseEntity<SkillSearchResponse> searchByNameNoOffset(
        @Size(max = MAX_SKILL_NAME_LENGTH, message = SKILL_NAME_OVER_MAX_LENGTH)
        @RequestParam(required = false) String keyword,
        String lastKeyword,
        int pageSize
    ) {
        return ResponseEntity.ok()
            .body(skillService.searchByNameNoOffset(keyword, lastKeyword, pageSize));
    }

}
