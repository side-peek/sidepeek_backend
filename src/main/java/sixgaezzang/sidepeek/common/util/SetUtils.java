package sixgaezzang.sidepeek.common.util;

import static sixgaezzang.sidepeek.common.util.CommonConstant.BLANK_STRING;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SetUtils {

    public static String getBlankIfNullOrBlank(String value) {
        return StringUtils.isBlank(value) ? BLANK_STRING : value;
    }

}
