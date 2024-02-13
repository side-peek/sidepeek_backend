package sixgaezzang.sidepeek.common.skill;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 300)
    private String name;

    @Column(name = "icon_image_url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Builder
    public Skill(String name, String url) {
        this.name = name;
        this.url = url;
    }

}
