package sixgaezzang.sidepeek.projects.util;

import static io.micrometer.common.util.StringUtils.isBlank;
import static sixgaezzang.sidepeek.projects.util.FakeValueProvider.createLongText;
import static sixgaezzang.sidepeek.projects.util.FakeValueProvider.createNickname;
import static sixgaezzang.sidepeek.projects.util.FakeValueProvider.createOverview;
import static sixgaezzang.sidepeek.projects.util.FakeValueProvider.createProjectName;
import static sixgaezzang.sidepeek.projects.util.FakeValueProvider.createSkillName;
import static sixgaezzang.sidepeek.projects.util.FakeValueProvider.createUrl;

import java.time.YearMonth;
import net.datafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sixgaezzang.sidepeek.comments.domain.Comment;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.User;

public class FakeEntityProvider {

    private static final Faker FAKER = new Faker();

    public static Project createProject(User user) {
        YearMonth startDate = YearMonth.now();
        YearMonth endDate = startDate.plusMonths(3);

        return Project.builder()
            .name(createProjectName())
            .subName(createProjectName())
            .overview(createOverview())
            .thumbnailUrl(createUrl())
            .githubUrl(createUrl())
            .startDate(startDate)
            .endDate(endDate)
            .ownerId(user.getId())
            .description(createLongText())
            .build();
    }

    public static User createUser() {
        String email = FakeValueProvider.createEmail();
        String password = FakeValueProvider.createPassword();

        return User.builder()
            .email(email)
            .password(new Password(password, new BCryptPasswordEncoder()))
            .nickname(createNickname())
            .build();
    }

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

    public static Skill createSkill() {
        return Skill.builder()
            .name(createSkillName())
            .iconImageUrl(createUrl())
            .build();
    }

    public static Comment createComment(User user, Project project) {
        return Comment.builder()
            .user(user)
            .project(project)
            .content(FakeValueProvider.createContent())
            .build();
    }
}
