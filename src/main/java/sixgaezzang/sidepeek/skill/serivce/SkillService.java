package sixgaezzang.sidepeek.skill.serivce;

import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateMaxLength;
import static sixgaezzang.sidepeek.skill.domain.Skill.MAX_SKILL_NAME_LENGTH;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_NOT_EXISTING;

import jakarta.persistence.EntityNotFoundException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.dto.response.SkillSearchResponse;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    private static void validateKeywordLength(String lastKeyword) {
        validateMaxLength(lastKeyword, MAX_SKILL_NAME_LENGTH,
            "최대 " + MAX_SKILL_NAME_LENGTH + "자의 키워드로 검색할 수 있습니다.");
    }

    public SkillSearchResponse searchByName(String keyword) {
        if (Objects.isNull(keyword) || keyword.isBlank()) {
            return SkillSearchResponse.from(skillRepository.findAll());
        }

        validateKeywordLength(keyword);

        return SkillSearchResponse.from(skillRepository.findAllByNameContaining(keyword));
    }

    public Slice<Skill> searchByNameOffset(String keyword, Pageable pageable) {
        validateKeywordLength(keyword);

        return skillRepository.findAllByNameStartingWithOrderByName(keyword, pageable);
    }

    public SkillSearchResponse searchByNameNoOffset(String keyword, String lastKeyword, int pageSize) {
        validateKeywordLength(keyword);

        return SkillSearchResponse.from(
            skillRepository.findAllByNameStartingWithAndNameGreaterThanOrderByName(keyword, lastKeyword,
                Limit.of(pageSize)));
    }

    public Skill getById(Long skillId) {
        return skillRepository.findById(skillId)
            .orElseThrow(() -> new EntityNotFoundException(SKILL_NOT_EXISTING));
    }

}
