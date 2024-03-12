package sixgaezzang.sidepeek.common.util;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SetUtils {

    public static boolean isSetPossible(String originalValue, String newValues) {
        return StringUtils.isBlank(originalValue) || !Objects.equals(originalValue, newValues);
    }

    public static <T> boolean isSetPossible(T originalValue, T newValues) {
        return Objects.isNull(originalValue) || !Objects.equals(originalValue, newValues);
    }

}
