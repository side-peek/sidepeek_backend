package sixgaezzang.sidepeek.projects.domain;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.skill.domain.Skill;

@Entity
@Table(name = "project_skill")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProjectSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    @Column(name = "project_id")
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", insertable = false, updatable = false)
    private Skill skill;

    @Column(name = "skill_id")
    private Long skillId;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Builder
    public ProjectSkill(Project project, Long projectId, Skill skill, Long skillId,
                        String category) {
        this.project = project;
        this.projectId = projectId;
        this.skill = skill;
        this.skillId = skillId;
        this.category = category;
    }

}
