package sixgaezzang.sidepeek.auth.domain;

import static sixgaezzang.sidepeek.auth.util.AuthConstant.MAX_PROVIDER_ID_LENGTH;
import static sixgaezzang.sidepeek.auth.util.AuthConstant.MAX_PROVIDER_TYPE_LENGTH;

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
import sixgaezzang.sidepeek.common.domain.BaseTimeEntity;
import sixgaezzang.sidepeek.users.domain.User;

@Entity
@Table(name = "auth_provider")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthProvider extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "provider_type", length = MAX_PROVIDER_TYPE_LENGTH, nullable = false, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "provider_id", length = MAX_PROVIDER_ID_LENGTH)
    private String providerId;

    @Builder
    private AuthProvider(User user, ProviderType providerType, String providerId) {
        this.user = user;
        this.providerType = providerType;
        this.providerId = providerId;
    }

}
