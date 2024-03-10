package sixgaezzang.sidepeek.util;

import static io.micrometer.common.util.StringUtils.isBlank;

import java.time.YearMonth;
import java.util.Objects;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.User;

public class FakeEntityProvider {

    // Project
    public static Project createProject(User user) {
        YearMonth startDate = YearMonth.now();
        YearMonth endDate = startDate.plusMonths(3);

        return Project.builder()
            .name(FakeValueProvider.createProjectName())
            .subName(FakeValueProvider.createProjectName())
            .overview(FakeValueProvider.createOverview())
            .thumbnailUrl(FakeValueProvider.createUrl())
            .githubUrl(FakeValueProvider.createUrl())
            .startDate(startDate)
            .endDate(endDate)
            .ownerId(user.getId())
            .description(FakeValueProvider.createLongText())
            .build();
    }

    // User
    public static User createUser() {
        String email = FakeValueProvider.createEmail();
        String password = FakeValueProvider.createPassword();

        return User.builder()
            .email(email)
            .password(new Password(password, new BCryptPasswordEncoder()))
            .nickname(FakeValueProvider.createNickname())
            .build();
    }

    // User
    public static User createUser(
        String email, String password, String nickname, PasswordEncoder passwordEncoder
    ) {
        return User.builder()
            .email(isBlank(email) ? FakeValueProvider.createEmail() : email)
            .password(isBlank(password) ? new Password(FakeValueProvider.createPassword(), passwordEncoder)
                : new Password(password, passwordEncoder))
            .nickname(isBlank(nickname) ? FakeValueProvider.createNickname() : nickname)
            .build();
    }

    // Skill
    public static Skill createSkill() {
        return Skill.builder()
            .name(FakeValueProvider.createSkillName())
            .iconImageUrl(FakeValueProvider.createUrl())
            .build();
    }

    // Comment
    public static Comment createComment(User user, Project project, Comment parent, boolean isAnonymous) {
        return Comment.builder()
            .user(user)
            .project(Objects.nonNull(project) ? project : parent.getProject())
            .parent(parent)
            .isAnonymous(isAnonymous)
            .content(FakeValueProvider.createContent())
            .build();
    }

    public static Comment createComment(User user, Project project, Comment parent) {
        return Comment.builder()
            .user(user)
            .project(Objects.nonNull(project) ? project : parent.getProject())
            .parent(parent)
            .isAnonymous(false)
            .content(FakeValueProvider.createContent())
            .build();
    }

}
