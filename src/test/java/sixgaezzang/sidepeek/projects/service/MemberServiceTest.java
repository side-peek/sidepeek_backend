package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
import sixgaezzang.sidepeek.projects.util.DomainProvider;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberServiceTest {

    static final Faker faker = new Faker();

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
                    new MemberSaveRequest(savedUser.getId(), null, "role" + i)
                );
                overLengthMembers.add(
                    new MemberSaveRequest(null, "nonFellowMember" + i, "role" + i)
                );
            }

            user = createAndSaveUser();
            overLengthMembers.add(
                new MemberSaveRequest(user.getId(), null, "role0")
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
        void 프로젝트가_null이어서_프로젝트_멤버_목록_저장에_실패한다() {
            // given
            Project nullProject = null;

            // when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.saveAll(nullProject, members);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage("프로젝트가 null 입니다.");
        }

        @Test
        void 목록_개수가_최대를_넘어서_멤버_목록_저장에_실패한다() {
            // given, when
            ThrowableAssert.ThrowingCallable saveAll = () -> memberService.saveAll(project, overLengthMembers);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveAll)
                .withMessage("멤버 수는 " + MAX_MEMBER_COUNT + "명 미만이어야 합니다.");
        }

        private static Stream<Arguments> createInvalidMemberInfo() {
            return Stream.of(
                Arguments.of("비회원 멤버 닉네임이 최대 길이를 넘는 경우",
                    false, "N".repeat(MAX_NICKNAME_LENGTH + 1), "role",
                    "비회원 멤버 닉네임은 " + MAX_NICKNAME_LENGTH + "자 미만이어야 합니다."),
                Arguments.of("비회원 멤버 역할을 적지 않는 경우",
                    false, "Nickname", null,
                    "멤버 역할 이름을 입력해주세요."),
                Arguments.of("비회원 멤버 역할이 최대 길이를 넘는 경우",
                    false, "Nickname", "R".repeat(MAX_ROLE_LENGTH + 1),
                    "멤버의 역할 이름은 " + MAX_ROLE_LENGTH + "자 미만이어야 합니다."),
                Arguments.of("회원 멤버 역할을 적지 않는 경우",
                    true, null, null,
                    "멤버 역할 이름을 입력해주세요."),
                Arguments.of("회원 멤버 역할이 최대 길이를 넘는 경우",
                    true, null, "R".repeat(MAX_ROLE_LENGTH + 1),
                    "멤버의 역할 이름은 " + MAX_ROLE_LENGTH + "자 미만이어야 합니다."),
                Arguments.of("비회원/회원 정보가 모두 없는 경우",
                    false, null, "role",
                    "회원인 멤버는 유저 Id를, 비회원인 멤버는 닉네임을 입력해주세요.")
            );
        }

        @Test
        void 존재하지_않는_회원이_멤버여서_멤버_목록_저장에_실패한다() {
            // given
            List<MemberSaveRequest> membersWithNonExistFellowMember = new ArrayList<>(members);
            membersWithNonExistFellowMember.add(
                new MemberSaveRequest(user.getId() + 1, null, "role")
            );

            // when
            ThrowableAssert.ThrowingCallable saveAll =
                () -> memberService.saveAll(project, membersWithNonExistFellowMember);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(saveAll)
                .withMessage("User Id에 해당하는 회원이 없습니다.");
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("createInvalidMemberInfo")
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
            user = DomainProvider.createUser();
            return userRepository.save(user);
        }

        private Project createAndSaveProject(User user) {
            project = DomainProvider.createProject(user);
            return projectRepository.save(project);
        }
    }
}
