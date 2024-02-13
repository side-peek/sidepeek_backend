package sixgaezzang.sidepeek.projects.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.users.domain.User;

@Entity
@Table(name = "project_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "authority", nullable = false, length = 15, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    private ProjectAuthority authority;

    @Column(name = "role", nullable = false, length = 15)
    private String role;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Builder
    public ProjectMember(User user, Project project, ProjectAuthority authority, String role, String nickname) {
        this.user = user;
        this.project = project;
        this.authority = authority;
        this.role = role;
        this.nickname = nickname;
    }

}
