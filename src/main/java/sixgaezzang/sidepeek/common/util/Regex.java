package sixgaezzang.sidepeek.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Regex {
    public static final String URL_REGEXP =
        "^(https?)://([^:/\\s]+)(:([^/]*))?((/[^\\s/]+)*)?/?([^#\\s?]*)(\\?([^#\\s]*))?(#(\\w*))?$";
}
