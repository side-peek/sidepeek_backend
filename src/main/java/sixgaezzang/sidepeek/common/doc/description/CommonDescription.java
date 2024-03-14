package sixgaezzang.sidepeek.common.doc.description;

import static sixgaezzang.sidepeek.common.util.CommonConstant.GITHUB_URL;
import static sixgaezzang.sidepeek.common.util.CommonConstant.MAX_TEXT_LENGTH;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonDescription {
    public static final String GITHUB_URL_DESCRIPTION =
        "Github URL, " + MAX_TEXT_LENGTH + "자 이하, " + GITHUB_URL + "로 시작";
}
