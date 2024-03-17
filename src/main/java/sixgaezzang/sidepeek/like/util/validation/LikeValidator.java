package sixgaezzang.sidepeek.like.util.validation;

import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.LIKE_ID_IS_NULL;
import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.LIKE_OWNER_ID_NOT_EQUALS_LOGIN_ID;
import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.PROJECT_ID_IS_NULL;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateOwnerId;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;
import sixgaezzang.sidepeek.like.dto.request.LikeRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LikeValidator {

    // Common
    public static void validateLikeId(Long likeId) {
        Assert.notNull(likeId, LIKE_ID_IS_NULL);
    }

    public static void validateLikeRequest(LikeRequest request) {
        if (Objects.isNull(request.projectId())) {
            throw new IllegalArgumentException(PROJECT_ID_IS_NULL);
        }
    }

    public static void validateLoginIdEqualsLikeOwnerId(Long loginId, Long ownerId) {
        validateOwnerId(ownerId);
        if (!loginId.equals(ownerId)) {
            throw new AccessDeniedException(LIKE_OWNER_ID_NOT_EQUALS_LOGIN_ID);
        }
    }

}
