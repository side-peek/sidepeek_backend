package sixgaezzang.sidepeek.skill.domain;

import static sixgaezzang.sidepeek.common.util.ValidationUtils.validateMaxLength;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "skill")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Skill {

    public static final int MAX_SKILL_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = MAX_SKILL_NAME_LENGTH)
    private String name;

    @Column(name = "icon_image_url", nullable = false, columnDefinition = "TEXT")
    private String iconImageUrl;

    @Builder
    public Skill(String name, String iconImageUrl) {
        validateMaxLength(name, MAX_SKILL_NAME_LENGTH,
            "최대 " + MAX_SKILL_NAME_LENGTH + "자의 이름으로 생성할 수 있습니다.");

        this.name = name;
        this.iconImageUrl = iconImageUrl;
    }

}
