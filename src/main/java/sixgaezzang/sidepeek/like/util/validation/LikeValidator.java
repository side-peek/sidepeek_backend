package sixgaezzang.sidepeek.like.util.validation;

import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.LIKE_ID_IS_NULL;
import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.PROJECT_ID_IS_NULL;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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

}
