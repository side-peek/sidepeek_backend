package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_OVER_MAX_COUNT;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_IS_NULL;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
import sixgaezzang.sidepeek.projects.dto.request.MemberSaveRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.repository.MemberRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.projects.util.FakeDtoProvider;
import sixgaezzang.sidepeek.projects.util.FakeEntityProvider;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;

    @Nested
    class 멤버_저장_테스트 {

        static final int MEMBER_COUNT = MAX_MEMBER_COUNT / 2;
        static List<MemberSaveRequest> members;
        static List<MemberSaveRequest> overLengthMembers;
        Project project;
        User user;

        @BeforeEach
        void setup() {
            overLengthMembers = new ArrayList<>();
            for (int i = 1; i <= MAX_MEMBER_COUNT / 2; i++) {
                User savedUser = createAndSaveUser();
                overLengthMembers.add(
                    FakeDtoProvider.createFellowMemberSaveRequest(savedUser.getId())
                );
                overLengthMembers.add(
                    FakeDtoProvider.createNonFellowMemberSaveRequest()
                );
            }

            user = createAndSaveUser();
            overLengthMembers.add(
                FakeDtoProvider.createFellowMemberSaveRequest(user.getId())
            );
            project = createAndSaveProject(user);
            members = overLengthMembers.subList(0, MEMBER_COUNT);
        }

        @Test
        void 프로젝트_멤버_목록_저장에_성공한다() {
            // given, when
            List<MemberSummary> savedMembers = memberService.saveAll(project, members);

            // then
            assertThat(savedMembers).hasSize(MEMBER_COUNT);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 빈_멤버_목록_저장은_무시되어_성공한다(List<MemberSaveRequest> emptyMembers) {
            // given, when
            List<MemberSummary> savedMembers = memberService.saveAll(project, emptyMembers);

            // then
            assertThat(savedMembers).isNull();
        }

        @Test
        void 목록_개수가_최대를_넘어서_멤버_목록_저장에_실패한다() {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.saveAll(project, overLengthMembers);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(MEMBER_OVER_MAX_COUNT);
        }

        @Test
        void 프로젝트가_null이어서_프로젝트_멤버_목록_저장에_실패한다() {
            // given
            Project nullProject = null;

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.saveAll(nullProject, members);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(PROJECT_IS_NULL);
        }

        @Test
        void 존재하지_않는_회원이_멤버여서_멤버_목록_저장에_실패한다() {
            // given
            List<MemberSaveRequest> membersWithNonExistFellowMember = new ArrayList<>(members);
            membersWithNonExistFellowMember.add(
                FakeDtoProvider.createFellowMemberSaveRequest(user.getId() + 1)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> memberService.saveAll(project, membersWithNonExistFellowMember);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(saveAll)
                .withMessage("User Id에 해당하는 회원이 없습니다.");
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.projects.util.TestParameterProvider#createInvalidMemberInfo")
        void 정보가_유효하지_않은_멤버여서_멤버_목록_저장에_실패한다(
            String testMessage, boolean isFellow, String nickname, String role, String message
        ) {
            // given
            List<MemberSaveRequest> membersWithInvalidMember = new ArrayList<>(members);
            membersWithInvalidMember.add(
                new MemberSaveRequest(isFellow ? user.getId() : null, nickname, role)
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.saveAll(project, membersWithInvalidMember);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage(message);
        }

        private User createAndSaveUser() {
            user = FakeEntityProvider.createUser();
            return userRepository.save(user);
        }

        private Project createAndSaveProject(User user) {
            project = FakeEntityProvider.createProject(user);
            return projectRepository.save(project);
        }
    }
}
