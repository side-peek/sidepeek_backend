package sixgaezzang.sidepeek.skill.serivce;

import static sixgaezzang.sidepeek.common.ValidationUtils.validateMaxLength;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.dto.SkillResponse;
import sixgaezzang.sidepeek.skill.dto.SkillSearchResponse;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;

@Service
@RequiredArgsConstructor
public class SkillService {

    public static final int KEYWORD_MAX_LENGTH = 50;

    private final SkillRepository skillRepository;

    public SkillSearchResponse searchByName(String keyword) {
        List<Skill> skillList;
        if (Objects.isNull(keyword) || keyword.isBlank()) {
            skillList = skillRepository.findAll();
        } else {
            validateMaxLength(keyword, KEYWORD_MAX_LENGTH, "최대 " + KEYWORD_MAX_LENGTH + "자의 키워드로 검색할 수 있습니다.");
            skillList = skillRepository.findAllByNameContaining(keyword);
        }

        List<SkillResponse> response = skillList.stream()
            .map(skill -> new SkillResponse(skill.getId(), skill.getName(), skill.getIconImageUrl()))
            .toList();

        return new SkillSearchResponse(response);
    }
}
