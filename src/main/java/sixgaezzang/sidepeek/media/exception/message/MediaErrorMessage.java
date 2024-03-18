package sixgaezzang.sidepeek.media.exception.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MediaErrorMessage {

    public static final String CONTENT_TYPE_IS_UNSUPPORTED = "지원하는 Content-Type이 아닙니다.";
    public static final String FILE_IS_EMPTY = "파일과 함께 요청을 보내주세요.";
    public static final String FILE_IS_INVALID = "이미지 혹은 영상 파일만 가능합니다.";
    public static final String CANNOT_READ_FILE = "파일을 읽는 도중 예외가 발생했습니다.";

}
