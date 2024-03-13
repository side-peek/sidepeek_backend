package sixgaezzang.sidepeek.like.util.validation;

import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.PROJECT_ID_IS_NULL;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.like.dto.request.LikeRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LikeValidator {

    public static void validateLikeRequest(LikeRequest request) {
        if (Objects.isNull(request.projectId())) {
            throw new IllegalArgumentException(PROJECT_ID_IS_NULL);
        }
    }

}
