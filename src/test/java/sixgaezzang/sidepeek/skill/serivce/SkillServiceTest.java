package sixgaezzang.sidepeek.skill.serivce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.skill.domain.Skill.MAX_SKILL_NAME_LENGTH;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.util.FakeEntityProvider;
import sixgaezzang.sidepeek.util.FakeValueProvider;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.dto.response.SkillResponse;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SkillServiceTest {

    @Autowired
    SkillService skillService;
    @Autowired
    SkillRepository skillRepository;

    static final int SKILL_COUNT = 10;
    List<String> skillNames;

    @BeforeEach
    void setUp() {
        skillNames = new ArrayList<>();
        for (int i = 0; i < SKILL_COUNT; i++) {
            skillNames.add(createAndSaveSkill().getName());
        }
    }

    private Skill createAndSaveSkill() {
        return skillRepository.save(FakeEntityProvider.createSkill());
    }

    @Nested
    class 기술_스택_검색_테스트 {

        @ParameterizedTest(name = "[{index}] {0}으로 검색할 때 " + SKILL_COUNT + "개의 모든 기술 스택이 나온다.")
        @NullAndEmptySource
        void 검색어_없이_전체_기술_스택_검색에_성공한다(String keyword) {
            // given, when
            List<SkillResponse> skills = skillService.searchByName(keyword)
                .skills();

            // then
            assertThat(skills.size()).isEqualTo(SKILL_COUNT);
        }

        @Test
        void 검색어로_기술_스택_검색에_성공한다() {
            // given
            String keyword = FakeValueProvider.createEnglishKeyword();
            int count = 0;
            for (String name : skillNames) {
                if (name.contains(keyword)) {
                    count++;
                }
            }

            // when
            List<SkillResponse> skills = skillService.searchByName(keyword)
                .skills();

            // then
            assertThat(skills.size()).isEqualTo(count);
        }

        @Test
        void 검색어_최대_글자_수가_넘어_기술_스택_검색에_실패한다() {
            // given
            int keywordLength = MAX_SKILL_NAME_LENGTH + 1;
            String keyword = "a".repeat(keywordLength);

            // when
            ThrowableAssert.ThrowingCallable search = () -> skillService.searchByName(keyword);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(search)
                .withMessage("최대 " + MAX_SKILL_NAME_LENGTH + "자의 키워드로 검색할 수 있습니다.");
        }

    }

}
