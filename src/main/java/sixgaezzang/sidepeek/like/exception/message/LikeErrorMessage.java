package sixgaezzang.sidepeek.like.exception.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LikeErrorMessage {

    // Common
    public static final String LIKE_IS_DUPLICATED = "이미 좋아요를 눌렀습니다.";
    public static final String LIKE_ID_IS_NULL = "좋아요 ID를 입력해주세요.";
    public static final String LIKE_NOT_EXISTING = "존재하지 않는 좋아요 입니다.";

    // projectId
    public static final String PROJECT_ID_IS_NULL = "프로젝트 ID를 입력해주세요.";

    // userId
    public static final String LIKE_OWNER_ID_NOT_EQUALS_LOGIN_ID = "좋아요한 회원의 Id가 로그인한 회원 Id와 일치하지 않습니다.";

}
