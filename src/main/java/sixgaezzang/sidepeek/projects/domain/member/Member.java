package sixgaezzang.sidepeek.projects.domain.member;

import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.projects.util.validation.MemberValidator.validateRole;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateProject;
import static sixgaezzang.sidepeek.users.util.UserConstant.MAX_NICKNAME_LENGTH;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateNickname;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.users.domain.User;

@Entity
@Table(name = "project_member")
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "role", nullable = false, length = MAX_ROLE_LENGTH)
    private String role;

    @Column(name = "nickname", nullable = false, length = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Builder
    public Member(User user, Project project, String role, String nickname) {
        validateConstructorRequiredArguments(project, role, nickname);

        // Required
        this.project = project;
        this.role = role;
        this.nickname = nickname;

        // Option
        this.user = user;
    }

    private void validateConstructorRequiredArguments(Project project, String role, String nickname) {
        validateProject(project);
        validateRole(role);
        validateNickname(nickname);
    }

}
