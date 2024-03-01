package sixgaezzang.sidepeek.projects.domain.member;

import static sixgaezzang.sidepeek.projects.exception.message.MemberErrorMessage.MEMBER_IS_INVALID;
import static sixgaezzang.sidepeek.projects.util.ProjectConstant.MAX_ROLE_LENGTH;
import static sixgaezzang.sidepeek.users.domain.User.MAX_NICKNAME_LENGTH;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.util.validation.MemberValidator;
import sixgaezzang.sidepeek.projects.util.validation.ProjectValidator;
import sixgaezzang.sidepeek.users.domain.User;

@Entity
@Table(name = "project_member")
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

    @Column(name = "nickname", length = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Builder
    public Member(User user, Project project, String role, String nickname) {
        validateConstructorRequiredArguments(project, role);
        validateConstructorMemberInfoArguments(user, nickname);

        // Required
        this.project = project;
        this.role = role;
        this.user = user;
        this.nickname = nickname;
    }

    private void validateConstructorMemberInfoArguments(User user, String nickname) {
        if (Objects.nonNull(user)) { // 회원인 경우
            MemberValidator.validateFellowMemberUser(user);
            return;
        }
        if (Objects.nonNull(nickname)) { // 비회원인 경우
            MemberValidator.validateNonFellowMemberNickname(nickname);
            return;
        }

        throw new IllegalArgumentException(MEMBER_IS_INVALID);
    }

    private void validateConstructorRequiredArguments(Project project, String role) {
        ProjectValidator.validateProject(project);
        MemberValidator.validateRole(role);
    }

}
