package sixgaezzang.sidepeek.projects.util;

import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import java.time.YearMonth;
import net.datafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.users.domain.Password;
import sixgaezzang.sidepeek.users.domain.User;

public class DomainProvider {
    private static final Faker FAKER = new Faker();

    public static Project createProject(User user) {
        String name = FAKER.internet().domainName();
        String subName = FAKER.internet().domainWord();
        String overview = FAKER.lorem().sentence();
        String thumbnailUrl = FAKER.internet().url();
        String githubUrl = FAKER.internet().url();
        YearMonth startDate = YearMonth.now();
        YearMonth endDate = startDate.plusMonths(3);
        String description = FAKER.lorem().sentences(10).toString();

        return Project.builder()
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
    }

    public static User createUser() {
        String email = FAKER.internet().emailAddress();
        String password = FAKER.internet().password(8, 40, true, true, true);
        String nickname = FAKER.internet().username();
        if (nickname.length() > MAX_NICKNAME_LENGTH) {
            nickname = nickname.substring(nickname.length() - MAX_NICKNAME_LENGTH);
        }

        return User.builder()
            .email(email)
            .password(new Password(password, new BCryptPasswordEncoder()))
            .nickname(nickname)
            .build();
    }

    public static Skill createSkill(String name) {
        String iconImageUrl = FAKER.internet().url();

        return Skill.builder()
            .name(name)
            .iconImageUrl(iconImageUrl)
            .build();
    }
}
