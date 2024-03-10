package sixgaezzang.sidepeek.users.domain;

import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_CATEGORY_LENGTH;
import static sixgaezzang.sidepeek.common.util.validation.TechStackValidator.validateCategory;
import static sixgaezzang.sidepeek.skill.util.validation.SkillValidator.validateSkill;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateUser;

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
import sixgaezzang.sidepeek.skill.domain.Skill;

@Entity
@Table(name = "user_skill")
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(name = "category", nullable = false, length = MAX_CATEGORY_LENGTH)
    private String category;

    @Builder
    public UserSkill(User user, Skill skill, String category) {
        validateConstructorArguments(user, skill, category);
        this.user = user;
        this.skill = skill;
        this.category = category;
    }

    private void validateConstructorArguments(User user, Skill skill, String category) {
        validateUser(user);
        validateSkill(skill);
        validateCategory(category);
    }

}
