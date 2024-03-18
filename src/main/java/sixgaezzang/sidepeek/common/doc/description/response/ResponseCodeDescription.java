package sixgaezzang.sidepeek.common.doc.description.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseCodeDescription {
    public static final String OK_200_DESCRIPTION = "[OK] 요청 성공 후 Body와 함께 응답";
    public static final String CREATED_201_DESCRIPTION = "[CREATED] 생성 요청 성공 후 Location(Header), Body(옵션)와 함께 응답";
    public static final String NO_CONTENT_204_DESCRIPTION = "[NO_CONTENT] 요청 성공 후 Body 없이 응답";
    public static final String BAD_REQUEST_400_DESCRIPTION =
        "[BAD_REQUEST] 요청 형식이 잘못되었거나, 요청 값이 유효하지 않은 경우 메시지와 응답(여러 필드 값이 유효하지 않은 경우, List로 응답)";
    public static final String UNAUTHORIZED_401_DESCRIPTION = "[UNAUTHORIZED] 로그인이 필요한 요청에서 로그인하지 않고 요청한 경우";
    public static final String FORBIDDEN_403_DESCRIPTION = "[FORBIDDEN] 로그인했지만 요청 권한이 없는 경우";
    public static final String NOT_FOUND_404_DESCRIPTION = "[NOT_FOUND] 요청한 자원이 없는 자원인 경우";
    public static final String CONFLICT_409_DESCRIPTION = "[CONFLICT] 같은 요청을 다시 요청한 경우(ex. 좋아요 등록 재요청)";
    public static final String INTERNAL_SERVER_ERROR_500_DESCRIPTION =
        "[INTERNAL_SERVER_ERROR] 서버에서 예측하지 못한 예외, 외부 시스템 문제가 발생한 경우";
}
