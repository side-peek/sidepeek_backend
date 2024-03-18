package sixgaezzang.sidepeek.projects.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.LOGIN_IS_REQUIRED;
import static sixgaezzang.sidepeek.common.exception.message.CommonErrorMessage.OWNER_ID_NOT_EQUALS_LOGIN_ID;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TECH_STACK_COUNT;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.OWNER_ID_IS_NULL;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_NOT_EXISTING;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.BANNER_PROJECT_COUNT;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_MEMBER_COUNT;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_ID_NOT_EQUALS_LOGIN_ID;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;
import static sixgaezzang.sidepeek.util.FakeDtoProvider.createFellowSaveMemberRequest;
import static sixgaezzang.sidepeek.util.FakeDtoProvider.createSaveProjectRequestOnlyRequired;
import static sixgaezzang.sidepeek.util.FakeDtoProvider.createSaveProjectRequestWithOwnerIdAndOption;
import static sixgaezzang.sidepeek.util.FakeDtoProvider.createSaveTechStackRequests;
import static sixgaezzang.sidepeek.util.FakeDtoProvider.createUpdateProjectRequestOnlyRequired;
import static sixgaezzang.sidepeek.util.FakeEntityProvider.createComment;
import static sixgaezzang.sidepeek.util.FakeEntityProvider.createLike;
import static sixgaezzang.sidepeek.util.FakeEntityProvider.createProject;
import static sixgaezzang.sidepeek.util.FakeEntityProvider.createSkill;
import static sixgaezzang.sidepeek.util.FakeEntityProvider.createUser;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createContent;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createGithubUrl;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createId;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createLongText;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createOverview;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createProjectName;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createRole;
import static sixgaezzang.sidepeek.util.FakeValueProvider.createUserProjectSearchType;

import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.datafaker.Faker;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.comments.dto.response.CommentResponse;
import sixgaezzang.sidepeek.comments.repository.CommentRepository;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;
import sixgaezzang.sidepeek.common.dto.response.Page;
import sixgaezzang.sidepeek.common.exception.InvalidAuthenticationException;
import sixgaezzang.sidepeek.common.util.component.DateTimeProvider;
import sixgaezzang.sidepeek.like.domain.Like;
import sixgaezzang.sidepeek.like.repository.LikeRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.UserProjectSearchType;
import sixgaezzang.sidepeek.projects.domain.member.Member;
import sixgaezzang.sidepeek.projects.dto.request.SaveMemberRequest;
import sixgaezzang.sidepeek.projects.dto.request.SaveProjectRequest;
import sixgaezzang.sidepeek.projects.dto.request.UpdateProjectRequest;
import sixgaezzang.sidepeek.projects.dto.response.ProjectBannerResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectListResponse;
import sixgaezzang.sidepeek.projects.dto.response.ProjectResponse;
import sixgaezzang.sidepeek.projects.repository.FileRepository;
import sixgaezzang.sidepeek.projects.repository.ProjectSkillRepository;
import sixgaezzang.sidepeek.projects.repository.member.MemberRepository;
import sixgaezzang.sidepeek.projects.repository.project.ProjectRepository;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectServiceTest {

    static final Faker faker = new Faker();
    static final int MEMBER_COUNT = MAX_MEMBER_COUNT / 2;
    static final int SKILL_COUNT = MAX_TECH_STACK_COUNT / 2;
    static List<SaveMemberRequest> members;
    static List<Long> fellowMemberIds;
    static List<SaveTechStackRequest> techStacks;
    static String NAME = createProjectName();
    static String OVERVIEW = createOverview();
    static String GITHUB_URL = createGithubUrl();
    static String DESCRIPTION = createLongText();

    @MockBean
    DateTimeProvider dateTimeProvider;

    @Autowired
    ProjectService projectService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    ProjectSkillRepository projectSkillRepository;

    @Autowired
    FileRepository fileRepository;

    User user;

    private User createAndSaveUser() {
        User newUser = createUser();
        return userRepository.save(newUser);
    }

    private Project createAndSaveProject(User user) {
        Project newProject = createProject(user);
        return projectRepository.save(newProject);
    }

    private ProjectResponse getNewSavedProject(Long userId) {
        SaveProjectRequest request = createSaveProjectRequestOnlyRequired(
            NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, userId, techStacks, members
        );
        return projectService.save(userId, request);
    }

    private Comment createAndSaveComment(User user, Project project, Comment comment) {
        Comment newComment = createComment(user, project, comment);
        return commentRepository.save(newComment);
    }

    private Like createAndSaveLike(Project project, User user) {
        Like newLike = createLike(user, project);
        return likeRepository.save(newLike);
    }

    @BeforeEach
    void setup() {
        members = new ArrayList<>();
        fellowMemberIds = new ArrayList<>();
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= MEMBER_COUNT - 1; i++) {
            Long savedUserId = createAndSaveUser().getId();
            fellowMemberIds.add(savedUserId);
            members.add(createFellowSaveMemberRequest(savedUserId));
            users.add(createUser());
        }
        userRepository.saveAll(users)
            .forEach(user -> {
                fellowMemberIds.add(user.getId());
                members.add(createFellowSaveMemberRequest(user.getId()));
            });

        user = createAndSaveUser();
        fellowMemberIds.add(0, user.getId());
        members.add(0, createFellowSaveMemberRequest(user.getId()));

        List<Skill> skills = new ArrayList<>();
        for (int i = 1; i <= SKILL_COUNT; i++) {
            skills.add(createSkill());
        }

        List<Long> createdSkillIds = skillRepository.saveAll(skills)
            .stream()
            .map(Skill::getId)
            .toList();

        techStacks = createSaveTechStackRequests(createdSkillIds);
    }

    @Nested
    class 프로젝트_상세_조회_테스트 {

        @Test
        void 사용자가_로그인하지_않아도_프로젝트_상세_조회를_성공한다() {
            // given
            Project project = createAndSaveProject(user);
            Comment comment = createAndSaveComment(user, project, null);
            CommentResponse commentResponse = CommentResponse.from(comment, true, List.of());

            // when
            ProjectResponse response = projectService.findById(null, project.getId());

            // then
            assertThat(response).extracting("id", "ownerId", "viewCount", "comments", "likeId")
                .containsExactly(project.getId(), user.getId(), 1L, List.of(commentResponse),
                    null);    // 이때, 좋아요 Id는 null
        }

        @Test
        void 로그인한_사용자가_프로젝트_상세_조회를_성공한다() {
            // given
            Project project = createAndSaveProject(user);
            Comment comment = createAndSaveComment(user, project, null);
            CommentResponse commentResponse = CommentResponse.from(comment, true, List.of());

            // when
            ProjectResponse response = projectService.findById(user.getId(), project.getId());

            // then
            assertThat(response).extracting("id", "ownerId", "viewCount", "comments", "likeId")
                .containsExactly(project.getId(), user.getId(), 1L, List.of(commentResponse), null);
        }

        @Test
        void 로그인한_사용자가_좋아요한_프로젝트_상세_조회를_성공한다() {
            // given
            Project project = createAndSaveProject(user);
            Comment comment = createAndSaveComment(user, project, null);
            CommentResponse commentResponse = CommentResponse.from(comment, true, List.of());
            Like like = createAndSaveLike(project,
                user);   // 사용자가 프로젝트에 좋아요를 누르면 상세 조회 시 좋아요 식별자 반환

            // when
            ProjectResponse response = projectService.findById(user.getId(), project.getId());

            // then
            assertThat(response).extracting("id", "ownerId", "viewCount", "comments", "likeId")
                .containsExactly(project.getId(), user.getId(), 1L, List.of(commentResponse),
                    like.getId());
        }

        @Test
        void 프로젝트_ID가_존재하지_않으면_프로젝트_상세_조회를_실패한다() {
            // given
            Long invalidId = faker.random().nextLong(Long.MAX_VALUE);

            // when
            ThrowingCallable findById = () -> projectService.findById(user.getId(), invalidId);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(findById)
                .withMessage(PROJECT_NOT_EXISTING);
        }
    }

    @Nested
    class 지난_주_인기_프로젝트_조회_테스트 {

        LocalDate nextSunday;

        @BeforeEach
        void setUp() {
            LocalDate today = LocalDate.now();
            if (!today.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                today = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            }

            nextSunday = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        }

        @Test
        void 최대_5개로_지난_주_인기_프로젝트_조회를_성공한다() {
            // given
            int overBannerProjectCount = BANNER_PROJECT_COUNT * 2;
            for (int i = 0; i < overBannerProjectCount; i++) {
                Project project = createAndSaveProject(user);
                User newUser = createAndSaveUser();
                createAndSaveLike(project, newUser);
            }

            // when
            given(dateTimeProvider.getCurrentDate()).willReturn(nextSunday);
            List<ProjectBannerResponse> responses = projectService.findAllPopularLastWeek();

            // then
            assertThat(responses).hasSize(BANNER_PROJECT_COUNT);
        }

        @Test
        void 좋아요를_많이_받은_순으로_지난_주_인기_프로젝트_조회를_성공한다() {
            // TODO: project likeCount 반영 후 테스트 예정
            //            // given
            //            List<Project> projects = new ArrayList<>();
            //            for (int i = 0; i < BANNER_PROJECT_COUNT; i++) {
            //                Project project = createAndSaveProject(user);
            //                projects.add(project);
            //                for (int j = 0; j < i + 1; j++) {
            //                    User newUser = createAndSaveUser();
            //                    createAndSaveLike(project, newUser);
            //                }
            //            }
            //            projects.sort(Comparator.comparing(Project::getLikeCount));
            //            List<ProjectBannerResponse> expectResponses = projects.stream()
            //                .map(ProjectBannerResponse::from)
            //                .toList();
            //
            //            // when
            //            given(dateTimeProvider.getCurrentDate()).willReturn(nextSunday);
            //            List<ProjectBannerResponse> responses = projectService.findAllPopularLastWeek();
            //
            //            // then
            //            assertThat(responses).isEqualTo(expectResponses);
        }

        @Test
        void 지난_주에_좋아요_기록이_없어_빈_배열로_지난_주_인기_프로젝트_조회를_성공한다() {
            // given, when
            given(dateTimeProvider.getCurrentDate()).willReturn(nextSunday);
            List<ProjectBannerResponse> responses = projectService.findAllPopularLastWeek();

            // then
            assertThat(responses).isEmpty();
        }

    }

    @Nested
    class 회원_관련_프로젝트_전체_조회_테스트 {

        static int defaultPageSize = 12;
        User user;
        List<Project> projects;
        long projectCount;

        @BeforeEach
        void setup() {
            user = createAndSaveUser();
            projectCount = faker.random().nextLong(1, 50);
            projects = new ArrayList<>();
            for (int i = 0; i < projectCount; i++) {
                projects.add(createProject(user));
            }
            projectRepository.saveAll(projects);
        }

        @Test
        void 유저가_참여한_프로젝트_조회에_성공한다() {
            // given
            Long loginId = user.getId();
            joinProjects(user, projects);

            // when
            Page<ProjectListResponse> expected = projectService.findByUser(user.getId(), loginId,
                UserProjectSearchType.JOINED, Pageable.ofSize(defaultPageSize));

            // then
            assertThat(expected.data()).isNotEmpty();
            assertThat(expected).extracting("totalElements", "totalPages", "pageSize", "pageNumber")
                .containsExactly(projectCount, calculatePageNumber(projectCount, defaultPageSize),
                    defaultPageSize, 0);
        }

        @Test
        void 유저가_좋아요_한_프로젝트_조회에_성공한다() {
            // given
            Long loginId = user.getId();
            likeProjects(user, projects);

            // when
            Page<ProjectListResponse> expected = projectService.findByUser(user.getId(), loginId,
                UserProjectSearchType.LIKED, Pageable.ofSize(defaultPageSize));

            // then
            assertThat(expected.data()).isNotEmpty();
            assertThat(expected).extracting("totalElements", "totalPages", "pageSize", "pageNumber")
                .containsExactly(projectCount, calculatePageNumber(projectCount, defaultPageSize),
                    defaultPageSize, 0);
        }

        @Test
        void 유저가_댓글_단_프로젝트_조회에_성공한다() {
            // given
            Long loginId = user.getId();
            commentOnProjects(user, projects);

            // when
            Page<ProjectListResponse> expected = projectService.findByUser(user.getId(), loginId,
                UserProjectSearchType.COMMENTED, Pageable.ofSize(defaultPageSize));

            // then
            assertThat(expected.data()).isNotEmpty();
            assertThat(expected).extracting("totalElements", "totalPages", "pageSize", "pageNumber")
                .containsExactly(projectCount, calculatePageNumber(projectCount, defaultPageSize),
                    defaultPageSize, 0);
        }

        @Test
        void 사용자가_존재하지_않는_경우_사용자_프로젝트_조회에_실패한다() {
            // given
            Long invalidUserId = createId();
            UserProjectSearchType type = createUserProjectSearchType();

            // when
            ThrowingCallable findByUser = () -> projectService.findByUser(invalidUserId,
                user.getId(), type, Pageable.ofSize(defaultPageSize));

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(findByUser)
                .withMessage(USER_NOT_EXISTING);
        }

        @Test
        void 로그인하지_않은_사용자일_경우_좋아요_한_프로젝트_조회에_실패한다() {
            // when
            ThrowingCallable findByUser = () -> projectService.findByUser(user.getId(), null,
                UserProjectSearchType.LIKED, Pageable.ofSize(defaultPageSize));

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(findByUser)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 로그인하지_않은_사용자일_경우_댓글_단_프로젝트_조회에_실패한다() {
            // when
            ThrowingCallable findByUser = () -> projectService.findByUser(user.getId(), null,
                UserProjectSearchType.COMMENTED, Pageable.ofSize(defaultPageSize));

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(findByUser)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 본인이_아닐_경우_좋아요_한_프로젝트_조회에_실패한다() {
            // given
            User anotherUser = createAndSaveUser();
            likeProjects(user, projects);

            // when
            ThrowingCallable findByUser = () -> projectService.findByUser(user.getId(),
                anotherUser.getId(),
                UserProjectSearchType.LIKED, Pageable.ofSize(defaultPageSize));

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(findByUser)
                .withMessage(USER_ID_NOT_EQUALS_LOGIN_ID);
        }

        @Test
        void 본인이_아닐_경우_댓글_단_프로젝트_조회에_실패한다() {
            // given
            User anotherUser = createAndSaveUser();
            commentOnProjects(user, projects);

            // when
            ThrowingCallable findByUser = () -> projectService.findByUser(user.getId(),
                anotherUser.getId(),
                UserProjectSearchType.COMMENTED, Pageable.ofSize(defaultPageSize));

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(findByUser)
                .withMessage(USER_ID_NOT_EQUALS_LOGIN_ID);
        }

        private void joinProjects(User user, List<Project> projects) {
            projects.stream()
                .forEach(project -> memberRepository.save(Member.builder()
                    .user(user)
                    .nickname(user.getNickname())
                    .project(project)
                    .role(createRole())
                    .build())
                );
        }

        private void likeProjects(User user, List<Project> projects) {
            projects.stream()
                .forEach(project -> likeRepository.save(Like.builder()
                    .user(user)
                    .project(project)
                    .build())
                );
        }

        private void commentOnProjects(User user, List<Project> projects) {
            projects.stream()
                .forEach(project -> commentRepository.save(Comment.builder()
                    .user(user)
                    .project(project)
                    .isAnonymous(false)
                    .content(createContent())
                    .build())
                );
        }

        private int calculatePageNumber(long totalElements, int pageSize) {
            return (int) Math.ceil((double) totalElements / pageSize);
        }
    }

    @Nested
    class 프로젝트_저장_테스트 {

        @Test
        void 필수_정보가_모두_포함되어_프로젝트_저장에_성공한다() {
            // given
            SaveProjectRequest request = createSaveProjectRequestOnlyRequired(
                NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId(), techStacks, members
            );

            // when
            ProjectResponse response = projectService.save(user.getId(), request);

            // then
            assertThat(response).extracting("name", "overview", "githubUrl", "description",
                    "ownerId")
                .containsExactly(NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId());
            assertThat(response.techStacks()).hasSize(techStacks.size());
            assertThat(response.members()).hasSize(members.size());
        }

        @ParameterizedTest(name = "[{index}] {0}이(가) 누락된 경우 실패한다.")
        @MethodSource("sixgaezzang.sidepeek.util.TestParameterProvider#createProjectsWithoutRequired")
        void 작성자_Id_외_필수_정보가_누락되어_프로젝트_저장에_실패한다(
            String testMessage, String name, String overview, String githubUrl, String description,
            String message
        ) {
            // given
            SaveProjectRequest request = createSaveProjectRequestOnlyRequired(
                name, overview, githubUrl, description, user.getId(), techStacks, members
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }

        @Test
        void 작성자_Id가_누락되어_프로젝트_저장에_실패한다() {
            // given
            SaveProjectRequest request = createSaveProjectRequestOnlyRequired(
                NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, null, techStacks, members
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(OWNER_ID_IS_NULL);
        }

        @Test
        void 작성자_Id가_로그인_Id와_불일치하여_프로젝트_저장에_실패한다() {
            // given
            SaveProjectRequest request = createSaveProjectRequestOnlyRequired(
                NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId() - 1, techStacks, members
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(saveProject)
                .withMessage(OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.util.TestParameterProvider#createProjectsOnlyInvalidRequired")
        void 유효하지_않은_필수_정보로_프로젝트_저장에_실패한다(
            String testMessage, String name, String overview, String githubUrl, String description,
            String message
        ) {
            // given
            SaveProjectRequest request = createSaveProjectRequestOnlyRequired(
                name, overview, githubUrl, description, user.getId(), techStacks, members
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("sixgaezzang.sidepeek.util.TestParameterProvider#createProjectsWithInvalidOption")
        void 유효하지_않은_옵션_정보로_프로젝트_저장에_실패한다(
            String testMessage, String subName, String thumbnailUrl, String deployUrl,
            String troubleShooting,
            YearMonth startDate, YearMonth endDate, String message
        ) {
            // given
            SaveProjectRequest request = createSaveProjectRequestWithOwnerIdAndOption(
                techStacks, user.getId(), subName, thumbnailUrl, deployUrl, troubleShooting,
                startDate, endDate
            );

            // when
            ThrowingCallable saveProject = () -> projectService.save(user.getId(), request);

            // then
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(saveProject)
                .withMessage(message);
        }

        @Test
        void 사용자가_로그인을_하지_않아서_프로젝트_저장에_실패한다() {
            // given
            SaveProjectRequest request = createSaveProjectRequestOnlyRequired(
                NAME, OVERVIEW, GITHUB_URL, DESCRIPTION, user.getId(), techStacks, members
            );

            // when
            ThrowingCallable save = () -> projectService.save(null, request);

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(save)
                .withMessage(LOGIN_IS_REQUIRED);
        }

    }

    @Nested
    class 프로젝트_수정_테스트 {

        @Test
        void 로그인한_사용자가_프로젝트_회원_멤버라서_기존_프로젝트_수정에_성공한다() {
            fellowMemberIds.forEach(fellowMemberId -> {
                // given
                ProjectResponse originalProject = getNewSavedProject(user.getId());

                // when
                String newName = createProjectName();
                String newOverview = createOverview();
                String newGithubUrl = createGithubUrl();
                String newDescription = createLongText();
                UpdateProjectRequest newRequest = createUpdateProjectRequestOnlyRequired(
                    newName, newOverview, newGithubUrl, newDescription, techStacks,
                    members
                );
                ProjectResponse savedProject = projectService.update(fellowMemberId,
                    originalProject.id(), newRequest);

                // then
                assertThat(savedProject).isNotEqualTo(originalProject);
                assertThat(savedProject).extracting("name", "overview", "githubUrl", "description")
                    .containsExactly(newName, newOverview, newGithubUrl, newDescription);
                assertThat(savedProject.techStacks()).hasSize(techStacks.size());
                assertThat(savedProject.members()).hasSize(members.size());
            });
        }

        @Test
        void 존재하지_않는_프로젝트_수정에_실패한다() {
            // given
            ProjectResponse originalProject = getNewSavedProject(user.getId());

            // when
            String newName = createProjectName();
            String newOverview = createOverview();
            String newGithubUrl = createGithubUrl();
            String newDescription = createLongText();
            UpdateProjectRequest newRequest = createUpdateProjectRequestOnlyRequired(
                newName, newOverview, newGithubUrl, newDescription, techStacks,
                members
            );
            ThrowingCallable update = () -> projectService.update(user.getId(),
                originalProject.id() + 1, newRequest);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(update)
                .withMessage(PROJECT_NOT_EXISTING);
        }

        @Test
        void 사용자가_로그인을_하지_않아서_프로젝트_수정에_실패한다() {
            // given
            ProjectResponse originalProject = getNewSavedProject(user.getId());

            // when
            String newName = createProjectName();
            String newOverview = createOverview();
            String newGithubUrl = createGithubUrl();
            String newDescription = createLongText();
            UpdateProjectRequest newRequest = createUpdateProjectRequestOnlyRequired(
                newName, newOverview, newGithubUrl, newDescription, techStacks, members
            );
            ThrowingCallable update = () -> projectService.update(null, originalProject.id(),
                newRequest);

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(update)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 로그인한_사용자가_프로젝트_회원_멤버가_아니라서_기존_프로젝트_수정에_실패한다() {
            List<Long> nonMemberIds = new ArrayList<>();
            for (int i = 1; i < 6; i++) {
                nonMemberIds.add(user.getId() + i);
            }

            nonMemberIds.forEach(nonMemberId -> {
                // given
                ProjectResponse originalProject = getNewSavedProject(user.getId());

                // when
                String newName = createProjectName();
                String newOverview = createOverview();
                String newGithubUrl = createGithubUrl();
                String newDescription = createLongText();
                UpdateProjectRequest newRequest = createUpdateProjectRequestOnlyRequired(
                    newName, newOverview, newGithubUrl, newDescription, techStacks,
                    members
                );
                ThrowingCallable update = () -> projectService.update(nonMemberId,
                    originalProject.id(), newRequest);

                // then
                assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(update)
                    .withMessage(ONLY_OWNER_AND_FELLOW_MEMBER_CAN_UPDATE);
            });
        }
    }

    @Nested
    class 프로젝트_삭제_테스트 {

        @Test
        void 프로젝트_소프트_삭제에_성공한다() {
            // given
            given(dateTimeProvider.getCurrentDateTime()).willReturn(LocalDateTime.now());
            ProjectResponse project = getNewSavedProject(user.getId());

            // when
            projectService.delete(user.getId(), project.id());

            // TODO: @SQLRestriction("deleted_at IS NULL")이 안먹힌다. 왜지?
            Optional<Project> deletedProject = projectRepository.findById(project.id());
            projectService.findById(user.getId(), project.id());

            // then
            assertThat(deletedProject).isNotEmpty();
            assertThat(deletedProject.get().getDeletedAt()).isNotNull();
        }

        @Test
        void 로그인하지_않은_사용자라서__프로젝트_삭제에_실패한다() {
            // given
            given(dateTimeProvider.getCurrentDateTime()).willReturn(LocalDateTime.now());
            ProjectResponse project = getNewSavedProject(user.getId());

            // when
            ThrowingCallable delete = () -> projectService.delete(null, project.id());

            // then
            assertThatExceptionOfType(InvalidAuthenticationException.class).isThrownBy(delete)
                .withMessage(LOGIN_IS_REQUIRED);
        }

        @Test
        void 존재하지_않는_프로젝트_삭제에_실패한다() {
            // given
            given(dateTimeProvider.getCurrentDateTime()).willReturn(LocalDateTime.now());
            ProjectResponse project = getNewSavedProject(user.getId());

            // when
            ThrowingCallable delete = () -> projectService.delete(user.getId(), project.id() + 1);

            // then
            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(delete)
                .withMessage(PROJECT_NOT_EXISTING);
        }

        @Test
        void 프로젝트_작성자가_아니라서_프로젝트_삭제에_실패한다() {
            // given
            given(dateTimeProvider.getCurrentDateTime()).willReturn(LocalDateTime.now());
            ProjectResponse project = getNewSavedProject(user.getId());

            User newUser = createAndSaveUser();

            // when
            ThrowingCallable delete = () -> projectService.delete(newUser.getId(), project.id());

            // then
            assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(delete)
                .withMessage(OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }

    }
}
