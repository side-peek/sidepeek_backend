package sixgaezzang.sidepeek.projects.domain;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_CATEGORY_LENGTH;

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
import sixgaezzang.sidepeek.projects.util.validation.ProjectValidator;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.util.validation.SkillValidator;

@Entity
@Table(name = "project_skill")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProjectSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(name = "category", nullable = false, length = MAX_CATEGORY_LENGTH)
    private String category;

    @Builder
    public ProjectSkill(Project project, Skill skill, String category) {
        validateConstructorArguments(project, skill, category);
        this.project = project;
        this.skill = skill;
        this.category = category;
    }

    private void validateConstructorArguments(Project project, Skill skill, String category) {
        ProjectValidator.validateProject(project);
        SkillValidator.validateSkill(skill);
        SkillValidator.validateCategory(category);
    }

}
