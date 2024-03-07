package sixgaezzang.sidepeek.auth.domain;

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

    public static final Boolean DEFAULT_IS_REGISTRATION_COMPLETE = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "provider_type", length = 50, nullable = false, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "provider_id", length = 100)
    private String providerId;

    @Column(name = "is_registration_complete", nullable = false, columnDefinition = "TINYINT")
    private boolean isRegistrationComplete = DEFAULT_IS_REGISTRATION_COMPLETE;

    @Builder
    private AuthProvider(User user, ProviderType providerType, String providerId,
        Boolean isRegistrationComplete) {
        this.user = user;
        this.providerType = providerType;
        this.providerId = providerId;
        this.isRegistrationComplete = isRegistrationComplete;
    }

}
