package sixgaezzang.sidepeek.users.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.TECH_STACKS_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_ID_IS_NULL;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_NOT_EXISTING;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_IS_NULL;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.common.dto.request.UpdateUserSkillRequest;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSkillSummary;
import sixgaezzang.sidepeek.users.repository.UserRepository;
import sixgaezzang.sidepeek.users.repository.userskill.UserSkillRepository;
import sixgaezzang.sidepeek.util.FakeDtoProvider;
import sixgaezzang.sidepeek.util.FakeEntityProvider;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserSkillServiceTest {

    static final int SKILL_COUNT = MAX_TECH_STACK_COUNT / 2;
    static List<UpdateUserSkillRequest> techStacks;
    static List<UpdateUserSkillRequest> overLengthTechStacks;
    @Autowired
    UserSkillService userSkillService;
    @Autowired
    UserSkillRepository userSkillRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    UserRepository userRepository;
    User user;
    Skill skill;

    @BeforeEach
    void setup() {
        List<Long> createdSkillIds = new ArrayList<>();
        for (int i = 1; i <= MAX_TECH_STACK_COUNT + 1; i++) {
            createdSkillIds.add(createAndSaveSkill().getId());
        }
        overLengthTechStacks = FakeDtoProvider.createUpdateUserSkillRequests(createdSkillIds);
        techStacks = overLengthTechStacks.subList(0, SKILL_COUNT);

        user = createAndSaveUser();
        skill = createAndSaveSkill();
    }

    private Skill createAndSaveSkill() {
        Skill skill = FakeEntityProvider.createSkill();
        return skillRepository.save(skill);
    }

    private User createAndSaveUser() {
        User newUser = FakeEntityProvider.createUser();
        return userRepository.save(newUser);
    }

    @Nested
    class 사용자_기술_스택_목록_조회_테스트 {

        @Test
        void 사용자_기술_스택_목록_조회에_성공한다() {
            // given
            List<UserSkillSummary> savedTechStacks = userSkillService.saveAll(user, techStacks);

            // when
            List<UserSkillSummary> retrievedTechStacks = userSkillService.findAllByUser(user);

            // then
            assertThat(retrievedTechStacks).hasSameSizeAs(savedTechStacks);
        }

        @Test
        void 기술_스택이_등록되지_않아_빈_사용자_기술_스택_목록_조회에_성공한다() {
            // given, when
            List<UserSkillSummary> retrievedTechStacks = userSkillService.findAllByUser(user);

            // then
            assertThat(retrievedTechStacks).isEmpty();
        }

        @ParameterizedTest
        @NullSource
        void 사용자가_null_이라서_사용자_기술_스택_목록_조회에_실패한다(User nullUser) {
            // given, when
            ThrowableAssert.ThrowingCallable findAllByUser = () -> userSkillService.findAllByUser(nullUser);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(findAllByUser)
                .withMessage(USER_IS_NULL);
        }

    }

    @Nested
    class 사용자_기술_스택_목록_수정_테스트 {

        @Test
        void 사용자_기술_스택_목록_수정에_성공한다() {
            // given
            List<UserSkillSummary> originalTechStacks = userSkillService.findAllByUser(user);

            // when
            List<UserSkillSummary> savedTechStacks = userSkillService.saveAll(user, techStacks);
            List<UserSkillSummary> retrievedTechStacks = userSkillService.findAllByUser(user);

            // then
            assertThat(retrievedTechStacks.size()).isNotEqualTo(originalTechStacks.size());
            assertThat(retrievedTechStacks).hasSameSizeAs(savedTechStacks);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 비어있는_사용자_기술_스택_목록_수정에_성공한다(List<UpdateUserSkillRequest> emptyTechStack) {
            // given, when
            userSkillService.saveAll(user, emptyTechStack);
            List<UserSkillSummary> retrievedTechStacks = userSkillService.findAllByUser(user);

            // then
            assertThat(retrievedTechStacks).isEmpty();
        }

        @ParameterizedTest
        @NullSource
        void 사용자가_null_이라서_사용자_기술_스택_목록_수정에_실패한다(User nullUser) {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> userSkillService.saveAll(nullUser, techStacks);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(USER_IS_NULL);
        }

        @Test
        void 목록_개수가_최대를_넘어서_사용자_기술_스택_목록_수정에_실패한다() {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> userSkillService.saveAll(user, overLengthTechStacks);

            // then
            AssertionsForClassTypes.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(TECH_STACKS_OVER_MAX_COUNT);

        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.util.TestParameterProvider#createInvalidTechStackInfo")
        void 기술_스택_카테고리가_유효하지_않아_사용자_기술_스택_목록_수정에_실패한다(
            String testMessage, String category, String message
        ) {
            // given
            List<UpdateUserSkillRequest> techStacksWithInvalidSkill = new ArrayList<>(techStacks);
            techStacksWithInvalidSkill.add(
                new UpdateUserSkillRequest(skill.getId(), category)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> userSkillService.saveAll(user, techStacksWithInvalidSkill);

            // then
            AssertionsForClassTypes.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(message);
        }

        @Test
        void 존재하지_않는_기술_스택_Id로_사용자_기술_스택_목록_수정에_실패한다() {
            // given
            List<UpdateUserSkillRequest> techStacksWithNonExistSkill = new ArrayList<>(techStacks);
            techStacksWithNonExistSkill.add(
                FakeDtoProvider.createUpdateUserSkillRequest(skill.getId() + 1)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> userSkillService.saveAll(user, techStacksWithNonExistSkill);

            // then
            AssertionsForClassTypes.assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(saveAll)
                .withMessage(SKILL_NOT_EXISTING);
        }

        @Test
        void 기술_스택_Id가_누락되어_사용자_기술_스택_목록_수정에_실패한다() {
            // given
            List<UpdateUserSkillRequest> techStacksWithNonExistSkill = new ArrayList<>(techStacks);
            techStacksWithNonExistSkill.add(
                FakeDtoProvider.createUpdateUserSkillRequest(null)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> userSkillService.saveAll(user, techStacksWithNonExistSkill);

            // then
            AssertionsForClassTypes.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(SKILL_ID_IS_NULL);
        }

        @Test
        void 같은_카테고리_내에_중복된_기술_스택으로_사용자_기술_스택_목록_수정에_실패한다() {
            // TODO: 유효성 검사로직 추가 필요
            // given

            // when

            // then

        }

    }

}
