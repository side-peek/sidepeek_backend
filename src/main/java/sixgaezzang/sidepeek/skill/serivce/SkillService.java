package sixgaezzang.sidepeek.skill.serivce;

import static sixgaezzang.sidepeek.common.ValidationUtils.validateMaxLength;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.skill.dto.SkillSearchResponse;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillService {

    public static final int KEYWORD_MAX_LENGTH = 50;

    private final SkillRepository skillRepository;

    public SkillSearchResponse searchByName(String keyword) {
        if (Objects.isNull(keyword) || keyword.isBlank()) {
            return SkillSearchResponse.from(skillRepository.findAll());
        }

        validateMaxLength(keyword, KEYWORD_MAX_LENGTH,
            "최대 " + KEYWORD_MAX_LENGTH + "자의 키워드로 검색할 수 있습니다.");

        return SkillSearchResponse.from(skillRepository.findAllByNameContaining(keyword));
    }

}
