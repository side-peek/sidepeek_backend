package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.TECH_STACKS_IS_NULL;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.TECH_STACKS_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.TECH_STACK_IS_DUPLICATED;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_IS_NULL;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_ID_IS_NULL;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_NOT_EXISTING;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.ProjectSkill;
import sixgaezzang.sidepeek.projects.dto.response.ProjectSkillSummary;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;
import sixgaezzang.sidepeek.util.FakeDtoProvider;
import sixgaezzang.sidepeek.util.FakeEntityProvider;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectSkillServiceTest {

    @Autowired
    ProjectSkillService projectSkillService;

    @Autowired
    ProjectSkillRepository projectSkillRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    ProjectRepository projectRepository;
    
    @Autowired
    UserRepository userRepository;

    static final int SKILL_COUNT = MAX_TECH_STACK_COUNT / 2;
    static List<SaveTechStackRequest> techStacks;
    static List<SaveTechStackRequest> overLengthTechStacks;
    Project project;
    User user;
    Skill skill;

    @BeforeEach
    void setup() {
        List<Long> createdSkillIds = new ArrayList<>();
        for (int i = 1; i <= MAX_TECH_STACK_COUNT + 1; i++) {
            createdSkillIds.add(createAndSaveSkill().getId());
        }
        overLengthTechStacks = FakeDtoProvider.createSaveTechStackRequests(createdSkillIds);
        techStacks = overLengthTechStacks.subList(0, SKILL_COUNT);

        user = createAndSaveUser();
        project = createAndSaveProject(user);
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

    private Project createAndSaveProject(User user) {
        Project newProject = FakeEntityProvider.createProject(user);
        return projectRepository.save(newProject);
    }

    @Nested
    class 프로젝트_기술_스택_목록_저장_테스트 {

        @Test
        void 프로젝트_기술_스택_목록_저장에_성공한다() {
            // given, when
            List<ProjectSkillSummary> savedTechStacks = projectSkillService.saveAll(project, techStacks);

            // then
            assertThat(savedTechStacks).hasSize(SKILL_COUNT);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 빈_기술_스택_목록_저장에_실패한다(List<SaveTechStackRequest> emptyTechStacks) {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> projectSkillService.saveAll(project, emptyTechStacks);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(TECH_STACKS_IS_NULL);
        }

        @Test
        void 프로젝트가_null이어서_프로젝트_기술_스택_목록_저장에_실패한다() {
            // given
            Project nullProject = null;

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> projectSkillService.saveAll(nullProject, techStacks);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(PROJECT_IS_NULL);
        }

        @Test
        void 목록_개수가_최대를_넘어서_기술_스택_목록_저장에_실패한다() {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> projectSkillService.saveAll(project, overLengthTechStacks);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(TECH_STACKS_OVER_MAX_COUNT);
        }

        @ParameterizedTest
        @MethodSource("sixgaezzang.sidepeek.util.TestParameterProvider#createInvalidTechStackInfo")
        void 기술_스택_카테고리가_유효하지_않아_기술_스택_목록_저장에_실패한다(
            String testMessage, String category, String message
        ) {
            // given
            List<SaveTechStackRequest> techStacksWithInvalidSkill = new ArrayList<>(techStacks);
            techStacksWithInvalidSkill.add(
                new SaveTechStackRequest(skill.getId(), category)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> projectSkillService.saveAll(project, techStacksWithInvalidSkill);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(message);
        }

        @Test
        void 존재하지_않는_기술_스택_Id로_기술_스택_목록_저장에_실패한다() {
            // given
            List<SaveTechStackRequest> techStacksWithNonExistSkill = new ArrayList<>(techStacks);
            techStacksWithNonExistSkill.add(
                FakeDtoProvider.createSaveTechStackRequest(skill.getId() + 1)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> projectSkillService.saveAll(project, techStacksWithNonExistSkill);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(saveAll)
                .withMessage(SKILL_NOT_EXISTING);
        }

        @Test
        void 기술_스택_Id가_누락되어_기술_스택_목록_저장에_실패한다() {
            // given
            List<SaveTechStackRequest> techStacksWithNonExistSkill = new ArrayList<>(techStacks);
            techStacksWithNonExistSkill.add(
                FakeDtoProvider.createSaveTechStackRequest(null)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> projectSkillService.saveAll(project, techStacksWithNonExistSkill);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(SKILL_ID_IS_NULL);
        }

        @Test
        void 같은_카테고리_내에_중복된_기술_스택으로_사용자_기술_스택_목록_저장에_실패한다() {
            // given
            SaveTechStackRequest techStack = techStacks.get(0);
            SaveTechStackRequest duplicatedTechStack = new SaveTechStackRequest(
                techStack.skillId(), techStack.category());
            techStacks.add(duplicatedTechStack);

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> projectSkillService.saveAll(project, techStacks);

            // then
            AssertionsForClassTypes.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(TECH_STACK_IS_DUPLICATED);
        }

    }

    @Nested
    class 프로젝트_기술_스택_목록_수정_테스트 {

        @Test
        void 기존_프로젝트_기술_스택_목록을_지우고_새로운_기술_스택_목록_수정에_성공한다() {
            // given
            projectSkillService.saveAll(project, techStacks);
            List<ProjectSkill> originalTechStacks = projectSkillService.findAll(project);

            // when
            List<SaveTechStackRequest> techStacksOnlyOne = new ArrayList<>();
            Skill newSkill = createAndSaveSkill();
            techStacksOnlyOne.add(FakeDtoProvider.createSaveTechStackRequest(newSkill.getId()));

            projectSkillService.saveAll(project, techStacksOnlyOne);
            List<ProjectSkill> savedTechStacks = projectSkillService.findAll(project);

            // then
            assertThat(originalTechStacks).isNotEqualTo(savedTechStacks);
            assertThat(savedTechStacks).hasSameSizeAs(techStacksOnlyOne);
        }

    }

}
