package sixgaezzang.sidepeek.skill.serivce;

import static sixgaezzang.sidepeek.common.ValidationUtils.validateMaxLength;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sixgaezzang.sidepeek.skill.dto.SkillSearchResponse;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;

@Service
@RequiredArgsConstructor
public class SkillService {

    public static final int KEYWORD_MAX_LENGTH = 50;

    private final SkillRepository skillRepository;

    public SkillSearchResponse searchByName(String searchKeyword) {
        if (Objects.isNull(searchKeyword) || searchKeyword.isBlank()) {
            return new SkillSearchResponse(skillRepository.findAll());
        }

        validateMaxLength(searchKeyword, KEYWORD_MAX_LENGTH, "최대 " + KEYWORD_MAX_LENGTH + "자의 키워드로 검색할 수 있습니다.");
        return new SkillSearchResponse(skillRepository.findAllByNameContaining(searchKeyword));
    }
}
