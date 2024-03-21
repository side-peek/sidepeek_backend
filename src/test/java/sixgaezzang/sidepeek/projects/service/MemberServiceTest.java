package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_IS_DUPLICATED;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_IS_EMPTY;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_NOT_INCLUDE_OWNER;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_IS_NULL;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;
import static sixgaezzang.sidepeek.util.FakeValueProvider.getMemberCountByRole;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
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
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.request.SaveMemberRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.repository.MemberRepository;
import sixgaezzang.sidepeek.projects.repository.project.ProjectRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;
import sixgaezzang.sidepeek.users.repository.UserRepository;
import sixgaezzang.sidepeek.util.FakeDtoProvider;
import sixgaezzang.sidepeek.util.FakeEntityProvider;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberServiceTest {

    static final int MEMBER_COUNT = MAX_MEMBER_COUNT / 2;
    static List<SaveMemberRequest> members;
    static int USER_INDEX = 0;
    static List<SaveMemberRequest> overLengthMembers;
    static int MEMBER_COUNT_BY_ROLE;

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;

    Project project;
    User user;

    @BeforeEach
    void setup() {
        overLengthMembers = new ArrayList<>();
        for (int i = 1; i <= MAX_MEMBER_COUNT / 2; i++) {
            overLengthMembers.add(
                FakeDtoProvider.createFellowSaveMemberRequest(createAndSaveUser().getId())
            );
            overLengthMembers.add(
                FakeDtoProvider.createNonFellowSaveMemberRequest()
            );
        }

        user = createAndSaveUser();

        overLengthMembers.add(USER_INDEX,
            FakeDtoProvider.createFellowSaveMemberRequest(user.getId()));
        members = overLengthMembers.subList(0, MEMBER_COUNT);
        MEMBER_COUNT_BY_ROLE = getMemberCountByRole(members);

        project = createAndSaveProject(user);
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
    class 멤버_저장_테스트 {

        @Test
        void 작성자를_포함한_프로젝트_멤버_목록_저장에_성공한다() {
            // given, when
            List<MemberSummary> savedMembers = memberService.cleanAndSaveAll(project, members);

            // then
            assertThat(savedMembers).hasSize(MEMBER_COUNT_BY_ROLE);
        }

        @Test
        void 닉네임을_기존과_다르게_설정한_회원_멤버를_포함하여_멤버_목록_저장에_성공한다() {
            // given
            Long userId = user.getId();
            String originalNickname = user.getNickname();

            // when
            List<MemberSummary> savedMembers = memberService.cleanAndSaveAll(project, members);
            Optional<UserSummary> fellowMember = savedMembers.stream()
                .flatMap(member -> member.userSummary().stream())
                .filter(user -> Objects.equals(user.id(), userId))
                .findFirst();

            // then
            assertThat(fellowMember).isNotEmpty();
            assertThat(originalNickname).isNotEqualTo(fellowMember.get().nickname());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 빈_멤버_목록_저장은_실패한다(List<SaveMemberRequest> emptyMembers) {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.cleanAndSaveAll(project,
                emptyMembers);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(MEMBER_IS_EMPTY);
        }

        @Test
        void 목록_개수가_최대를_넘어서_멤버_목록_저장에_실패한다() {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.cleanAndSaveAll(project,
                overLengthMembers);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(MEMBER_OVER_MAX_COUNT);
        }

        @Test
        void 프로젝트가_null이어서_프로젝트_멤버_목록_저장에_실패한다() {
            // given
            Project nullProject = null;

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.cleanAndSaveAll(
                nullProject, members);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(PROJECT_IS_NULL);
        }

        @Test
        void 존재하지_않는_회원이_멤버여서_멤버_목록_저장에_실패한다() {
            // given
            List<SaveMemberRequest> membersWithNonExistFellowMember = new ArrayList<>(members);
            membersWithNonExistFellowMember.add(
                FakeDtoProvider.createFellowSaveMemberRequest(user.getId() + 1)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> memberService.cleanAndSaveAll(project, membersWithNonExistFellowMember);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(saveAll)
                .withMessage(USER_NOT_EXISTING);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.util.TestParameterProvider#createInvalidMemberInfo")
        void 정보가_유효하지_않은_멤버여서_멤버_목록_저장에_실패한다(
            String testMessage, boolean isFellow, String nickname, String role, String message
        ) {
            // given
            List<SaveMemberRequest> membersWithInvalidMember = new ArrayList<>(members);
            membersWithInvalidMember.add(
                new SaveMemberRequest(isFellow ? user.getId() : null, nickname, role)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.cleanAndSaveAll(
                project, membersWithInvalidMember);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(message);
        }

        @Test
        void 작성자가_포함_되어있지_않아_멤버_목록_저장에_실패한다() {
            // given
            List<SaveMemberRequest> membersWithoutOwner = new ArrayList<>(members);
            membersWithoutOwner.remove(USER_INDEX);

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.cleanAndSaveAll(
                project, membersWithoutOwner);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(MEMBER_NOT_INCLUDE_OWNER);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.util.TestParameterProvider#createDuplicatedMembers")
        void 멤버의_역할과_닉네임이_중복이어서_멤버_목록_저장에_실패한다(
            String testMessage, Function<User, List<SaveMemberRequest>> getDuplicatedMembers
        ) {
            // given
            members.addAll(getDuplicatedMembers.apply(createAndSaveUser()));

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.cleanAndSaveAll(project,
                members);

            // then
            AssertionsForClassTypes.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(saveAll)
                .withMessage(MEMBER_IS_DUPLICATED);
        }

    }

    @Nested
    class 멤버_수정_테스트 {

        @Test
        void 기존_프로젝트_멤버_목록을_지우고_새로운_멤버_목록_수정에_성공한다() {
            // given
            memberService.cleanAndSaveAll(project, members);
            List<MemberSummary> originalMembers = memberService.findAll(project);

            // when
            List<SaveMemberRequest> membersOnlyOwner = new ArrayList<>();
            membersOnlyOwner.add(FakeDtoProvider.createFellowSaveMemberRequest(user.getId()));

            memberService.cleanAndSaveAll(project, membersOnlyOwner);
            List<MemberSummary> savedMembers = memberService.findAll(project);

            // then
            assertThat(originalMembers).isNotEqualTo(savedMembers);
            assertThat(savedMembers).hasSameSizeAs(membersOnlyOwner);
        }

    }
}
