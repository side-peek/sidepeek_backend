package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import net.datafaker.Faker;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.dto.request.MemberSaveRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.repository.MemberRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectRepository;
import sixgaezzang.sidepeek.users.domain.Password;
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
                User savedUser = createUser();
                overLengthMembers.add(
                    new MemberSaveRequest(savedUser.getId(), null, "role" + i)
                );
                overLengthMembers.add(
                    new MemberSaveRequest(null, "nonFellowMember" + i, "role" + i)
                );
            }

            user = createUser();
            overLengthMembers.add(
                new MemberSaveRequest(user.getId(), null, "role0")
            );
            project = createProject(user);
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


        private User createUser() {
            String email = faker.internet().emailAddress();
            String password = faker.internet().password(8, 40, true, true, true);
            String nickname = faker.internet().username();

            User user = User.builder()
                .email(email)
                .password(new Password(password, new BCryptPasswordEncoder()))
                .nickname(nickname)
                .build();
            return userRepository.save(user);
        }

        private Project createProject(User user) {
            String name = faker.internet().domainName();
            String subName = faker.internet().domainWord();
            String overview = faker.lorem().sentence();
            String thumbnailUrl = faker.internet().url();
            String githubUrl = faker.internet().url();
            YearMonth startDate = YearMonth.now();
            YearMonth endDate = startDate.plusMonths(3);
            String description = faker.lorem().sentences(10).toString();

            Project project = Project.builder()
                .name(name)
                .subName(subName)
                .overview(overview)
                .thumbnailUrl(thumbnailUrl)
                .githubUrl(githubUrl)
                .startDate(startDate)
                .endDate(endDate)
                .ownerId(user.getId())
                .description(description)
                .build();

            return projectRepository.save(project);
        }

    }
}
